package cn.easii.relation.core;

import cn.easii.relation.RelationCache;

public class DefaultRelationCache implements RelationCache {

    private final ExpireMap<String, Object> expireMap;

    public DefaultRelationCache() {
        expireMap = new ExpireMap<>();
    }

    @Override
    public boolean hasKey(final String key) {
        return expireMap.containsKey(key);
    }

    @Override
    public void set(final String key, final Object value, final long time) {
        expireMap.put(key, value, time);
    }

    @Override
    public Object get(final String key) {
        return expireMap.get(key);
    }

}
