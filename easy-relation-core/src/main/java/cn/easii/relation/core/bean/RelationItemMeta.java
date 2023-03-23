package cn.easii.relation.core.bean;

import cn.easii.relation.RelationExceptionStrategy;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelationItemMeta implements Serializable {

    private String field;

    private Method fieldGetter;

    private Method fieldSetter;

    private String targetField;

    private List<DynamicConditionMeta> conditions;

    private List<ConstantsConditionMeta> constantsConditions;

    private RelationExceptionStrategy exceptionStrategy;

}
