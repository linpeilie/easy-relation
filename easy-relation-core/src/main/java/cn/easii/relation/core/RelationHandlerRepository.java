package cn.easii.relation.core;

import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.bean.RelationHandlerMeta;
import cn.easii.relation.core.function.ExceptionFunction;
import cn.easii.relation.exception.RelationException;
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
            ExceptionFunction<Object, Object> methodInvoker = o -> method.invoke(relationService, o);
            final RelationHandlerMeta relationHandlerMeta =
                new RelationHandlerMeta(methodInvoker, parameterTypes[0]);
            final RelationHandlerMeta oldRelationHandler =
                relationHandlerMap.putIfAbsent(relationHandler.value(), relationHandlerMeta);
            if (oldRelationHandler != null) {
                throw new RelationException(
                    "The duplicate relation handler [" + relationHandler.value() +
                    "] is registered, please check the@RelationHandler annotation configuration\n");
            }
        }
    }

    public static RelationHandlerMeta getHandler(String handlerIdentifier) {
        return relationHandlerMap.get(handlerIdentifier);
    }

}
