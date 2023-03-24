package cn.easii.relation.core;

import cn.easii.relation.CacheStrategy;
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

    public InjectRelation(final RelationProperties relationProperties) {
        this(new DefaultRelationCache(), new JsonMapToBeanHandle(), relationProperties);
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
            SessionCache.closeCacheIfNecessary();
        }
    }

    /**
     * 开启一级缓存
     */
    private void openSessionCache() {
        SessionCache.openCacheIfNecessary();
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

    private boolean secondLevelCacheEnabled(RelationMeta relationMeta, DataProviderMeta dataProviderMeta) {
        switch (relationMeta.getCacheStrategy()) {
            case ENABLE:
                return true;
            case DISABLE:
                return false;
            case DEFAULT:
            default:
                return dataProviderMeta.isUseCache();
        }
    }

    private <T> void inject(T t, RelationMeta relationMeta, RelationItemMeta relationItemMeta)
        throws Exception {
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
        if (SessionCache.isOpen()) {
            cacheKey = getCacheKey(relationMeta.getDataProvider(), paramMap);
            if (SessionCache.exists(cacheKey)) {
                final Object result = SessionCache.get(cacheKey);
                handleResult(t, result, relationItemMeta);
                return;
            }
        }
        // 查询
        final DataProviderMeta dataProvider =
            DataProviderRepository.getDataProvider(relationMeta.getDataProvider());
        // 二级缓存，如果强制开启缓存则先判断缓存数据
        if (secondLevelCacheEnabled(relationMeta, dataProvider)) {
            cacheKey = cacheKey == null ? getCacheKey(relationMeta.getDataProvider(), paramMap) : cacheKey;
            if (relationCache.hasKey(cacheKey)) {
                final Object result = relationCache.get(cacheKey);
                handleResult(t, result, relationItemMeta);
                return;
            }
        }

        if (dataProvider == null) {
            throw new RelationException("cannot find data provider by " + relationMeta.getDataProvider());
        }
        // 映射参数
        final Object param = mapToBeanHandle.mapToBean(paramMap, dataProvider.getParameterClass());
        final Object result = dataProvider.getFunction().apply(param);
        // 保存一级缓存
        SessionCache.putIfAbsent(cacheKey, result);
        // 二级缓存
        if (secondLevelCacheEnabled(relationMeta, dataProvider)) {
            relationCache.set(cacheKey, result, dataProvider.getCacheTimeout() * 1000L);
        }
        // 解析结果
        if (result == null) {
            return;
        }
        handleResult(t, result, relationItemMeta);
    }

    private <T> void handleResult(T t, Object result, RelationItemMeta relationItemMeta)
        throws InvocationTargetException, IllegalAccessException {
        if (result == null) {
            return;
        }
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
                .append(StrUtil.DASHED)
                .append(ObjectUtils.toString(entry.getValue()));
        }
        return cacheKey.toString();
    }

}
