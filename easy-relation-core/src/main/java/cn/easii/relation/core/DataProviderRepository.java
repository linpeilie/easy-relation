package cn.easii.relation.core;

import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.bean.DataProviderMeta;
import cn.easii.relation.core.function.ExceptionFunction;
import cn.easii.relation.exception.RelationException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataProviderRepository {

    private static final Map<String, DataProviderMeta> relationHandlerMap = new ConcurrentHashMap<>();

    public static void registerProvider(DataProviderService dataProviderService) {
        final Method[] methods = dataProviderService.getClass().getMethods();
        for (Method method : methods) {
            final DataProvider dataProvider = method.getAnnotation(DataProvider.class);
            if (dataProvider == null) {
                continue;
            }
            // parameter
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 0) {
                continue;
            }
            // method invoke
            ExceptionFunction<Object, Object> methodInvoker = o -> method.invoke(dataProviderService, o);
            final DataProviderMeta dataProviderMeta =
                new DataProviderMeta(methodInvoker, parameterTypes[0]);
            final DataProviderMeta oldRelationHandler =
                relationHandlerMap.putIfAbsent(dataProvider.value(), dataProviderMeta);
            if (oldRelationHandler != null) {
                throw new RelationException(
                    "The duplicate relation handler [" + dataProvider.value() +
                    "] is registered, please check the@RelationHandler annotation configuration\n");
            }
        }
    }

    public static DataProviderMeta getDataProvider(String providerIdentifier) {
        return relationHandlerMap.get(providerIdentifier);
    }

}
