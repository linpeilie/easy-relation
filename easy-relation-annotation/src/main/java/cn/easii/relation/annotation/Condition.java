package cn.easii.relation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联条件
 *
 * @author linpl
 * @version 1.0
 */
@Documented
@Target({ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

    /**
     * 当前类的需要字段名称，关联查询时，会取配置的当前字段的值，从而关联查询
     *
     * @return {@link String} 当前类的字段名称
     */
    String field();

    /**
     * 关联条件字段，默认取 associateField() 中配置的属性名称
     *
     * @return {@link String} 关联条件字段名称
     */
    String paramField() default "";

}
