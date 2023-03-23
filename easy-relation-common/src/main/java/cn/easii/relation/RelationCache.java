package cn.easii.relation;

/**
 * 缓存管理器
 *
 * @author linpl
 * @version 1.0
 */
public interface RelationCache {

    /**
     * 指定键值是否存在
     *
     * @param key 键
     * @return 指定键值是否存在
     */
    boolean hasKey(String key);

    /**
     * 保存缓存
     *
     * @param key   键值
     * @param value 实际值
     * @param time  过期时间，单位：ms
     */
    void set(String key, Object value, long time);

    /**
     * 获取缓存数据
     *
     * @param key 键值
     * @return {@link Object} key对应的值
     */
    Object get(String key);

}
