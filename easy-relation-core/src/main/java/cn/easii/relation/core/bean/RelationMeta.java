package cn.easii.relation.core.bean;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 针对单个关联项的描述类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelationMeta implements Serializable {

    private String field;

    private Method fieldSetter;

    private String handlerIdentifier;

    private String targetField;

    private List<DynamicConditionMeta> conditions;

    private List<ConstantsConditionMeta> constantsConditions;

    private boolean useCache;

    private int cacheTimeout;

    private boolean throwException;

}
