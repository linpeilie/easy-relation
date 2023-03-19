package cn.easii.relation.core.function;

@FunctionalInterface
public interface ExceptionFunction<P, R> {

    R apply(P param) throws Exception;

}
