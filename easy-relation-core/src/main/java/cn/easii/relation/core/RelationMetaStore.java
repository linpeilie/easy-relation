package cn.easii.relation.core;

import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.ConstantsCondition;
import cn.easii.relation.annotation.Relation;
import cn.easii.relation.core.bean.ConstantsConditionMeta;
import cn.easii.relation.core.bean.DynamicConditionMeta;
import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.core.utils.ReflectUtils;
import cn.hutool.core.util.ReflectUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationMetaStore {

    private static final Map<Class<?>, List<RelationMeta>> relationMetaMap = new ConcurrentHashMap<>();

    public static List<RelationMeta> getRelationMetaList(Class<?> clazz) {
        if (relationMetaMap.containsKey(clazz)) {
            return relationMetaMap.get(clazz);
        }
        final Field[] fields = ReflectUtil.getFields(clazz);
        List<RelationMeta> relationMetas = new ArrayList<>();
        for (Field field : fields) {
            final Relation relation = field.getAnnotation(Relation.class);
            if (relation == null) {
                continue;
            }
            relationMetas.add(toMeta(clazz, field, relation));
        }
        relationMetaMap.put(clazz, relationMetas);
        return relationMetas;
    }

    private static RelationMeta toMeta(Class<?> clazz, Field field, Relation relation) {
        RelationMeta relationMeta = new RelationMeta();
        relationMeta.setField(field.getName());
        relationMeta.setFieldSetter(ReflectUtils.getSetter(clazz, field.getName()));
        relationMeta.setHandlerIdentifier(relation.handler());
        relationMeta.setTargetField(relation.targetField());
        relationMeta.setConditions(buildConditions(clazz, relation.condition()));
        relationMeta.setConstantsConditions(buildConditions(relation.constantsCondition()));
        relationMeta.setUseCache(relation.useCache());
        relationMeta.setCacheTimeout(relation.cacheTimeout());
        relationMeta.setThrowException(relation.throwException());
        return relationMeta;
    }

    private static List<DynamicConditionMeta> buildConditions(Class<?> clazz, Condition[] conditions) {
        List<DynamicConditionMeta> dynamicConditionMetas = new ArrayList<>();
        for (final Condition condition : conditions) {
            final DynamicConditionMeta dynamicConditionMeta = new DynamicConditionMeta();
            dynamicConditionMeta.setFieldGetter(ReflectUtils.getGetter(clazz, condition.field()));
            dynamicConditionMeta.setParamField(condition.paramField());
            dynamicConditionMetas.add(dynamicConditionMeta);
        }
        return dynamicConditionMetas;
    }

    private static List<ConstantsConditionMeta> buildConditions(ConstantsCondition[] conditions) {
        List<ConstantsConditionMeta> constantsConditionMetas = new ArrayList<>();
        for (final ConstantsCondition condition : conditions) {
            final ConstantsConditionMeta constantsConditionMeta = new ConstantsConditionMeta();
            constantsConditionMeta.setField(condition.field());
            constantsConditionMeta.setValue(condition.value());
            constantsConditionMetas.add(constantsConditionMeta);
        }
        return constantsConditionMetas;
    }

}
