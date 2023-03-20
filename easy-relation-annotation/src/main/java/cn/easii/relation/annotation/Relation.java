package cn.easii.relation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联属性 标注当前属性为关联其他对象的属性
 *
 * @author linpl
 * @date 2023/03/18
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {

    /**
     * 将处理程序
     *
     * @return {@link String}
     */
    String handler();

    /**
     * 关联对象的具体属性，但需要关联对象中的某个字段时，必须要指定该属性，否则，将认为直接把关联查询结果，赋值给该属性
     *
     * @return {@link String}
     */
    String targetField() default "";

    /**
     * 关联条件
     *
     * @return {@link Condition[]}
     */
    Condition[] condition();

    /**
     * 关联常量条件
     *
     * @return {@link ConstantsCondition[]}
     */
    ConstantsCondition[] constantsCondition() default {};

    /**
     * 使用缓存，默认 false
     *
     * @return boolean
     */
    boolean useCache() default false;

    /**
     * 缓存超时时间，单位：s
     *
     * @return int
     */
    int cacheTimeout() default 300;

    /**
     * 抛出异常
     *
     * @return boolean
     */
    boolean throwException() default false;

}
