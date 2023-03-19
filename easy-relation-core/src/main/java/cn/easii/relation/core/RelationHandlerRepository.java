package cn.easii.relation.core;

import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.bean.RelationHandlerMeta;
import cn.easii.relation.core.function.ExceptionFunction;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RelationHandlerRepository {

    private static final Map<String, RelationHandlerMeta> relationHandlerMap = new ConcurrentHashMap<>();

    public static void registerHandler(RelationService relationService) {
        final Method[] methods = relationService.getClass().getMethods();
        for (Method method : methods) {
            final RelationHandler relationHandler = method.getAnnotation(RelationHandler.class);
            if (relationHandler == null) {
                continue;
            }
            // parameter
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                continue;
            }
            // method invoke
            ExceptionFunction<Object, Object> methodInvoker = new ExceptionFunction<Object, Object>() {
                @Override
                public Object apply(final Object o) throws Exception {
                    return method.invoke(relationService, o);
                }
            };
            final RelationHandlerMeta relationHandlerMeta =
                new RelationHandlerMeta(methodInvoker, parameterTypes[0]);
            relationHandlerMap.put(relationHandler.value(), relationHandlerMeta);
        }
    }

    public static RelationHandlerMeta getHandler(String handlerIdentifier) {
        return relationHandlerMap.get(handlerIdentifier);
    }

}
