package cn.easii.relation.core.bean;

import cn.easii.relation.core.function.ExceptionFunction;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataProviderMeta implements Serializable {

    private ExceptionFunction<Object, Object> function;

    private Class<?> parameterClass;

    private boolean useCache;

    private int cacheTimeout;

}
