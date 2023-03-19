package cn.easii.relation.core.bean;

import java.lang.reflect.Method;
import lombok.Data;

@Data
public class DynamicConditionMeta {

    /**
     * 关联条件字段get方法
     */
    private Method fieldGetter;

    /**
     * 关联条件字段
     */
    private String paramField;

}
