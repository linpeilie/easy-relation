package cn.easii.relation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联常量条件，比如查询固定某状态的值
 *
 * @author linpl
 * @version 1.0
 */
@Documented
@Target({ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstantsCondition {

    /**
     * 对应关联查询时条件的字段名称
     *
     * @return {@link String}
     */
    String field();

    /**
     * 关联查询时，指定字段的值
     *
     * @return {@link String}
     */
    String value();

}
