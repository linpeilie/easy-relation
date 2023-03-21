package cn.easii.relation.core;

import cn.easii.relation.MapToBeanHandle;
import cn.easii.relation.RelationCache;
import cn.easii.relation.annotation.Relation;
import cn.easii.relation.annotation.RelationExceptionStrategy;
import cn.easii.relation.core.bean.ConstantsConditionMeta;
import cn.easii.relation.core.bean.DynamicConditionMeta;
import cn.easii.relation.core.bean.RelationHandlerMeta;
import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.core.properties.RelationProperties;
import cn.easii.relation.exception.RelationException;
import cn.easii.relation.core.function.InnerFunction;
import cn.easii.relation.core.utils.CollectionUtils;
import cn.easii.relation.core.utils.ObjectUtils;
import cn.easii.relation.core.utils.ReflectUtils;
import cn.hutool.core.date.StopWatch;
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

    public InjectRelation(final RelationCache relationCache, final MapToBeanHandle mapToBeanHandle, final RelationProperties relationProperties) {
        this.relationCache = relationCache;
        this.mapToBeanHandle = mapToBeanHandle;
        this.relationProperties = relationProperties;
    }

    public <T> void injectRelation(T t, String... relationFields) {
        cachedInvoke(() -> injectOn(t, relationFields));
    }

    public <T> void injectRelation(List<T> list, String... relationFields) {
        cachedInvoke(() -> list.forEach(t -> injectOn(t, relationFields)));
    }

    private void cachedInvoke(InnerFunction innerFunction) {
        try {
            resultTempMap.set(new HashMap<>());
            innerFunction.invoke();
        } finally {
            resultTempMap.get().clear();
            resultTempMap.remove();
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
            if (ArrayUtil.isNotEmpty(relationFields) && ArrayUtil.contains(relationFields, relationMeta.getField())) {
                continue;
            }
            try {
                inject(t, relationMeta);
            } catch (Exception e) {
                handleException(relationMeta.getExceptionStrategy(), e);
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

    private <T> void inject(T t, RelationMeta relationMeta) throws Exception {
        // 如果已经有值，则不再关联查询
        if (relationMeta.getFieldGetter().invoke(t) != null) {
            return;
        }
        // 初始化参数
        Map<String, Object> paramMap = new HashMap<>();
        for (DynamicConditionMeta condition : relationMeta.getConditions()) {
            paramMap.put(condition.getParamField(), condition.getFieldGetter().invoke(t));
        }
        for (ConstantsConditionMeta constantsCondition : relationMeta.getConstantsConditions()) {
            paramMap.put(constantsCondition.getField(), constantsCondition.getValue());
        }
        // 缓存 key
        final String cacheKey = getCacheKey(relationMeta.getHandlerIdentifier(), paramMap);
        // 获取一级缓存
        final Map<String, Object> tempMap = resultTempMap.get();
        if (tempMap.containsKey(cacheKey)) {
            final Object result = tempMap.get(cacheKey);
            handleResult(t, result, relationMeta);
            return;
        }
        // 二级缓存
        if (relationMeta.isUseCache()) {
            final Object result = relationCache.get(cacheKey);
            if (result != null) {
                handleResult(t, result, relationMeta);
                return;
            }
        }
        // 查询
        final RelationHandlerMeta relationHandler =
            RelationHandlerRepository.getHandler(relationMeta.getHandlerIdentifier());
        if (relationHandler == null) {
            throw new RelationException("cannot find relation handler by " + relationMeta.getHandlerIdentifier());
        }
        // 映射参数
        final Object param = mapToBeanHandle.mapToBean(paramMap, relationHandler.getParameterClass());
        final Object result = relationHandler.getHandlerFunction().apply(param);
        // 保存一级缓存
        tempMap.put(cacheKey, result);
        if (relationMeta.isUseCache()) {
            relationCache.set(cacheKey, result, relationMeta.getCacheTimeout() * 1000L);
        }
        // 解析结果
        if (result == null) {
            return;
        }
        final String targetField = relationMeta.getTargetField();
        if (StrUtil.isNotEmpty(targetField)) {
            final Method getter = ReflectUtils.getGetter(result.getClass(), targetField);
            final Object fieldValue = getter.invoke(result);
            handleResult(t, fieldValue, relationMeta);
        } else {
            handleResult(t, result, relationMeta);
        }
    }

    private <T> void handleResult(T t, Object result, RelationMeta relationMeta)
        throws InvocationTargetException, IllegalAccessException {
        relationMeta.getFieldSetter().invoke(t, result);
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
