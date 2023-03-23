package cn.easii.relation.core;

import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.ConstantsCondition;
import cn.easii.relation.annotation.Relation;
import cn.easii.relation.core.bean.ConstantsConditionMeta;
import cn.easii.relation.core.bean.DynamicConditionMeta;
import cn.easii.relation.core.bean.RelationItemMeta;
import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.core.utils.ReflectUtils;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
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
        Map<String, RelationMeta> metaMap = new HashMap<>();
        for (Field field : fields) {
            final Relation relation = field.getAnnotation(Relation.class);
            if (relation == null) {
                continue;
            }
            final String provider = relation.provider();
            RelationMeta relationMeta = metaMap.putIfAbsent(provider, new RelationMeta(provider));
            relationMeta = relationMeta == null ? metaMap.get(provider) : relationMeta;
            relationMeta.setUseCache(relationMeta.isUseCache() || relation.useCache());
            relationMeta.setCacheTimeout(Math.max(relationMeta.getCacheTimeout(), relation.cacheTimeout()));
            relationMeta.addItem(toItemMeta(clazz, field, relation));
        }
        List<RelationMeta> relationMetas = new ArrayList<>(metaMap.values());
        relationMetaMap.put(clazz, relationMetas);
        return relationMetas;
    }

    private static RelationItemMeta toItemMeta(Class<?> clazz, Field field, Relation relation) {
        return RelationItemMeta.builder()
            .field(field.getName())
            .fieldGetter(ReflectUtils.getGetter(clazz, field.getName()))
            .fieldSetter(ReflectUtils.getSetter(clazz, field.getName()))
            .targetField(relation.targetField())
            .conditions(buildConditions(clazz, relation.condition()))
            .constantsConditions(buildConditions(relation.constantsCondition()))
            .exceptionStrategy(relation.exceptionStrategy())
            .build();
    }

    private static List<DynamicConditionMeta> buildConditions(Class<?> clazz, Condition[] conditions) {
        List<DynamicConditionMeta> dynamicConditionMetas = new ArrayList<>();
        for (final Condition condition : conditions) {
            final DynamicConditionMeta dynamicConditionMeta = new DynamicConditionMeta();
            dynamicConditionMeta.setFieldGetter(ReflectUtils.getGetter(clazz, condition.field()));
            dynamicConditionMeta.setParamField(
                StrUtil.isEmpty(condition.paramField()) ? condition.field() : condition.paramField());
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
