package cn.easii.relation.core;

import cn.easii.relation.MapToBeanHandle;
import cn.easii.relation.RelationCache;
import cn.easii.relation.RelationExceptionStrategy;
import cn.easii.relation.core.bean.ConstantsConditionMeta;
import cn.easii.relation.core.bean.DynamicConditionMeta;
import cn.easii.relation.core.bean.DataProviderMeta;
import cn.easii.relation.core.bean.RelationItemMeta;
import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.properties.RelationProperties;
import cn.easii.relation.exception.RelationException;
import cn.easii.relation.core.function.InnerFunction;
import cn.easii.relation.core.utils.ObjectUtils;
import cn.easii.relation.core.utils.ReflectUtils;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InjectRelation {

    private static final ThreadLocal<Map<String, Object>> resultTempMap = new ThreadLocal<>();

    private final RelationCache relationCache;

    private final MapToBeanHandle mapToBeanHandle;

    private final RelationProperties relationProperties;

    public InjectRelation() {
        this(new DefaultRelationCache(), new JsonMapToBeanHandle(), new RelationProperties());
    }

    public InjectRelation(final RelationCache relationCache) {
        this(relationCache, new JsonMapToBeanHandle(), new RelationProperties());
    }

    public InjectRelation(final MapToBeanHandle mapToBeanHandle) {
        this(new DefaultRelationCache(), mapToBeanHandle, new RelationProperties());
    }

    public InjectRelation(final RelationCache relationCache,
        final MapToBeanHandle mapToBeanHandle,
        final RelationProperties relationProperties) {
        this.relationCache = relationCache;
        this.mapToBeanHandle = mapToBeanHandle;
        this.relationProperties = relationProperties;
    }

    public <T> void injectRelation(T t, String... relationFields) {
        cachedInvoke(() -> injectOn(t, relationFields));
    }

    public <T> void injectRelation(List<T> list, String... relationFields) {
        cachedInvoke(() -> list.forEach(t -> {
            openSessionCache();
            injectOn(t, relationFields);
        }));
    }

    private void cachedInvoke(InnerFunction innerFunction) {
        try {
            innerFunction.invoke();
        } finally {
            if (resultTempMap.get() != null) {
                resultTempMap.get().clear();
                resultTempMap.remove();
            }
        }
    }

    /**
     * 开启一级缓存
     */
    private void openSessionCache() {
        if (resultTempMap.get() == null) {
            resultTempMap.set(new HashMap<>());
        }
    }

    private <T> void injectOn(T t, String... relationFields) {
        if (t == null) {
            return;
        }
        final List<RelationMeta> relationMetaList = RelationMetaStore.getRelationMetaList(t.getClass());
        if (relationMetaList == null || relationMetaList.isEmpty()) {
            return;
        }
        for (RelationMeta relationMeta : relationMetaList) {
            if (relationMeta.getItems().isEmpty()) {
                continue;
            }
            for (RelationItemMeta relationMetaItem : relationMeta.getItems()) {
                // ignore field
                if (ArrayUtil.isNotEmpty(relationFields) &&
                    !ArrayUtil.contains(relationFields, relationMetaItem.getField())) {
                    continue;
                }

                try {
                    if (relationMeta.getItems().size() > 1) {
                        openSessionCache();
                    }
                    inject(t, relationMeta, relationMetaItem);
                } catch (Exception e) {
                    handleException(relationMetaItem.getExceptionStrategy(), e);
                }
            }
        }
    }

    private void handleException(final RelationExceptionStrategy exceptionStrategy, final Exception e) {
        switch (exceptionStrategy) {
            case IGNORE:
                break;
            case WARN:
                log.warn("an exception occurred while getting the relation data, error info : {}",
                    ExceptionUtil.stacktraceToString(e));
                break;
            case THROW:
                log.error("an exception occurred while getting the relation data, error info", e);
                throw new RelationException(e);
            default:
                if (relationProperties.getDefaultExceptionStrategy() != RelationExceptionStrategy.DEFAULT) {
                    handleException(relationProperties.getDefaultExceptionStrategy(), e);
                }
                break;
        }
    }

    private <T> void inject(T t, RelationMeta relationMeta, RelationItemMeta relationItemMeta)
        throws Exception {
        // 如果已经有值，则不再关联查询
        if (relationItemMeta.getFieldGetter().invoke(t) != null) {
            return;
        }
        // 初始化参数
        Map<String, Object> paramMap = new HashMap<>();
        for (DynamicConditionMeta condition : relationItemMeta.getConditions()) {
            paramMap.put(condition.getParamField(), condition.getFieldGetter().invoke(t));
        }
        for (ConstantsConditionMeta constantsCondition : relationItemMeta.getConstantsConditions()) {
            paramMap.put(constantsCondition.getField(), constantsCondition.getValue());
        }
        // 缓存 key
        String cacheKey = null;
        // 获取一级缓存
        final Map<String, Object> sessionCacheMap = resultTempMap.get();
        if (sessionCacheMap != null) {
            cacheKey = getCacheKey(relationMeta.getDataProvider(), paramMap);
            if (sessionCacheMap.containsKey(cacheKey)) {
                final Object result = sessionCacheMap.get(cacheKey);
                handleResult(t, result, relationItemMeta);
                return;
            }
        }
        // 二级缓存
        if (relationMeta.isUseCache()) {
            cacheKey = cacheKey == null ? getCacheKey(relationMeta.getDataProvider(), paramMap) : cacheKey;
            final Object result = relationCache.get(cacheKey);
            if (result != null) {
                handleResult(t, result, relationItemMeta);
                return;
            }
        }
        // 查询
        final DataProviderMeta relationHandler =
            DataProviderRepository.getDataProvider(relationMeta.getDataProvider());
        if (relationHandler == null) {
            throw new RelationException("cannot find relation handler by " + relationMeta.getDataProvider());
        }
        // 映射参数
        final Object param = mapToBeanHandle.mapToBean(paramMap, relationHandler.getParameterClass());
        final Object result = relationHandler.getFunction().apply(param);
        // 保存一级缓存
        if (sessionCacheMap != null) {
            sessionCacheMap.put(cacheKey, result);
        }
        if (relationMeta.isUseCache()) {
            relationCache.set(cacheKey, result, relationMeta.getCacheTimeout() * 1000L);
        }
        // 解析结果
        if (result == null) {
            return;
        }
        handleResult(t, result, relationItemMeta);
    }

    private <T> void handleResult(T t, Object result, RelationItemMeta relationItemMeta)
        throws InvocationTargetException, IllegalAccessException {
        Object r = result;
        if (relationItemMeta.getTargetField() != null && !relationItemMeta.getTargetField().isEmpty()) {
            final Method getter = ReflectUtils.getGetter(result.getClass(), relationItemMeta.getTargetField());
            r = getter.invoke(result);
        }
        relationItemMeta.getFieldSetter().invoke(t, r);
    }

    private String getCacheKey(String handlerIdentifier, Map<String, Object> paramMap) {
        StringBuilder cacheKey = new StringBuilder();
        cacheKey.append(handlerIdentifier);
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            cacheKey.append(StrUtil.DASHED)
                .append(entry.getKey())
                .append(ObjectUtils.toString(entry.getValue()));
        }
        return cacheKey.toString();
    }

}
