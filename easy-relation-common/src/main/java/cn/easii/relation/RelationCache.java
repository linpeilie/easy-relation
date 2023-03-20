package cn.easii.relation;

/**
 * 缓存管理器
 *
 * @author linpl
 * @date 2023/03/18
 */
public interface RelationCache {

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
