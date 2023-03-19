package cn.easii.relation.core.utils;

import cn.easii.relation.core.exception.RelationException;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Method getGetter(Class<?> clazz, String fieldName) {
        String getMethodName = StrUtil.upperFirstAndAddPre(fieldName, "get");
        final Method getMethod = ReflectUtil.getMethodByNameIgnoreCase(clazz, getMethodName);
        if (getMethod != null) {
            return getMethod;
        }
        String isMethodName = StrUtil.upperFirstAndAddPre(fieldName, "is");
        final Method isMethod = ReflectUtil.getMethodByNameIgnoreCase(clazz, isMethodName);
        if (isMethod != null) {
            return isMethod;
        }
        throw new RelationException("class : " + clazz + " cannot find getter method for field : " + fieldName);
    }

    public static Method getSetter(Class<?> clazz, String fieldName) {
        String setMethodName = StrUtil.upperFirstAndAddPre(fieldName, "set");
        final Method setMethod = ReflectUtil.getMethodByNameIgnoreCase(clazz, setMethodName);
        if (setMethod != null) {
            return setMethod;
        }
        throw new RelationException("class : " + clazz + " cannot find setter method for field : " + fieldName);
    }

}
