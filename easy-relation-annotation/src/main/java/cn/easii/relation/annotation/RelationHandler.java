package cn.easii.relation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联查询处理器，标记方法为关联查询方法
 *
 * @author linpl
 * @version 1.0
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationHandler {

    /**
     * 关联查询处理器唯一标识，不可重复
     *
     * @return {@link String}
     */
    String value();

}
