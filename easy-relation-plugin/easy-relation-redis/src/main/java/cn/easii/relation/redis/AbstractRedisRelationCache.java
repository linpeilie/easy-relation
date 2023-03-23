package cn.easii.relation.redis;

import cn.easii.relation.RelationCache;
import cn.easii.relation.properties.RelationProperties;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public abstract class AbstractRedisRelationCache implements RelationCache {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RelationProperties relationProperties;

    public AbstractRedisRelationCache(RedisConnectionFactory redisConnectionFactory,
        RelationProperties relationProperties) {
        this.relationProperties = relationProperties;
        this.redisTemplate = getRedisTemplate(redisConnectionFactory);
    }

    private RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer valueSerializer = valueSerializer();
        // 设置value的序列化规则和key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    protected abstract RedisSerializer valueSerializer();

    private String getKey(String key) {
        return relationProperties.getRedis().getKeyPrefix() == null ? key :
               relationProperties.getRedis().getKeyPrefix() + key;
    }

    @Override
    public boolean hasKey(final String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void set(final String key, final Object value, final long time) {
        redisTemplate.opsForValue().set(getKey(key), value, time, TimeUnit.MILLISECONDS);
    }

    @Override
    public Object get(final String key) {
        return redisTemplate.opsForValue().get(getKey(key));
    }

}
