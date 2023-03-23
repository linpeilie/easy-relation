package cn.easii.relation.annotation;

import cn.easii.relation.CacheStrategy;
import cn.easii.relation.Constants;
import cn.easii.relation.RelationExceptionStrategy;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联属性 标注当前属性为关联其他对象的属性
 *
 * @author linpl
 * @version 1.0
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {

    /**
     * 数据提供者
     *
     * @return {@link String}
     */
    String provider();

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
     * 缓存策略：
     * <ul>
     *     <li>{@link CacheStrategy#ENABLE}: 强制开启缓存</li>
     *     <li>{@link CacheStrategy#DEFAULT}: 默认，默认情况下以 DataProvider 上配置的为准</li>
     *     <li>{@link CacheStrategy#DISABLE}: 禁用缓存</li>
     * </ul>
     * 当配置为非 {@link CacheStrategy#DEFAULT} 时，对于该类的缓存策略以当前配置为准，否则以 DataProvider 配置为准。
     *
     * @return {@link CacheStrategy}
     */
    CacheStrategy cacheStrategy() default CacheStrategy.DEFAULT;

    /**
     * 缓存超时时间，单位：s
     *
     * @return int
     */
    int cacheTimeout() default Constants.DefaultCacheTime;

    /**
     * 当数据关联出现异常时的处理策略，默认在 RelationProperties 配置
     *
     * @return {@link RelationExceptionStrategy}
     */
    RelationExceptionStrategy exceptionStrategy() default RelationExceptionStrategy.DEFAULT;
}
