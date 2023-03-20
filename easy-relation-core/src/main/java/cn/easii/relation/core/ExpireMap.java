package cn.easii.relation.core;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class ExpireMap<K, V> {

    private final Map<K, V> map;

    private final Map<K, Long> EXPIRE_TIME_MAP;

    private final Timer timer;

    public ExpireMap() {
        map = new ConcurrentHashMap<>();
        EXPIRE_TIME_MAP = new ConcurrentHashMap<>();
        timer = new Timer();
        timer.schedule(new CheckKeyExpireTask(), 0, 1);
    }

    public V get(K key) {
        final Long expireTime = EXPIRE_TIME_MAP.get(key);
        if (expireTime == null || expireTime > System.currentTimeMillis()) {
            return null;
        }
        return map.get(key);
    }

    public V put(K key, V value, long expire) {
        EXPIRE_TIME_MAP.put(key, System.currentTimeMillis() + expire);
        return map.put(key, value);
    }

    class CheckKeyExpireTask extends TimerTask {
        @Override
        public void run() {
            for (Map.Entry<K, Long> entry : EXPIRE_TIME_MAP.entrySet()) {
                if (System.currentTimeMillis() > entry.getValue()) {
                    map.remove(entry.getKey());
                    EXPIRE_TIME_MAP.remove(entry.getKey());
                }
            }
        }
    }

}
