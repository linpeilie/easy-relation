package cn.easii.relation.core.utils;

import cn.hutool.core.util.StrUtil;

public class ObjectUtils {

    public static String toString(Object obj) {
        return obj == null ? StrUtil.EMPTY : obj.toString();
    }

}
