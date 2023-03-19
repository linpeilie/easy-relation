package cn.easii.relation.core;

import cn.easii.relation.RelationCache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;

public class DefaultRelationCache implements RelationCache {

    private final TimedCache<String, Object> timedCache;

    public DefaultRelationCache() {
        timedCache = CacheUtil.newTimedCache(1000 * 60 * 5);
        timedCache.schedulePrune(5);
    }

    @Override
    public void set(final String key, final Object value, final long time) {
        timedCache.put(key, value, time);
    }

    @Override
    public Object get(final String key) {
        return timedCache.get(key);
    }
}
