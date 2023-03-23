package cn.easii.relation.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话缓存
 */
class SessionCache {

    private static final ThreadLocal<Map<String, Object>> resultTempMap = new ThreadLocal<>();

    /**
     * 缓存是否开启
     *
     * @return true 开启 false 未开启
     */
    public static boolean isOpen() {
        return resultTempMap.get() != null;
    }

    /**
     * 如果没有开启缓存，则开启缓存
     */
    public static void openCacheIfNecessary() {
        if (isOpen()) {
            resultTempMap.set(new HashMap<>());
        }
    }

    /**
     * 如果已经开启缓存的话，则关闭缓存
     */
    public static void closeCacheIfNecessary() {
        if (isOpen()) {
            resultTempMap.get().clear();
            resultTempMap.remove();
        }
    }

    /**
     * 获取缓存的值，如果缓存没有开启，或者没有保存当前缓存的话，会返回null
     *
     * @param key 缓存的key
     * @return 缓存的值
     */
    public static Object get(String key) {
        if (isOpen()) {
            return resultTempMap.get().get(key);
        }
        return null;
    }

    /**
     * 如果缓存开启的话，则缓存当前key和value
     *
     * @param key   缓存key
     * @param value 缓存值
     */
    public static void putIfAbsent(String key, Object value) {
        if (isOpen()) {
            resultTempMap.get().put(key, value);
        }
    }

    /**
     * 判断指定缓存key是否存在
     *
     * @param key 缓存key
     * @return true 存在 false 不存在
     */
    public static boolean exists(String key) {
        if (isOpen()) {
            return resultTempMap.get().containsKey(key);
        }
        return false;
    }

}
