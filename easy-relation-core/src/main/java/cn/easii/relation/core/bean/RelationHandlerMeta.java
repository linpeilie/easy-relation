package cn.easii.relation.core.bean;

import cn.easii.relation.core.function.ExceptionFunction;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationHandlerMeta implements Serializable {

    private ExceptionFunction<Object, Object> handlerFunction;

    private Class<?> parameterClass;

}
