package cn.easii.relation.redis;

import cn.easii.relation.Constants;
import cn.easii.relation.properties.RelationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

public class JdkRedisRelationCache extends AbstractRedisRelationCache {
    public JdkRedisRelationCache(final RedisConnectionFactory redisConnectionFactory,
        RelationRedisProperties redisProperties) {
        super(redisConnectionFactory, redisProperties);
    }

    @Override
    protected RedisSerializer valueSerializer() {
        return new JdkSerializationRedisSerializer();
    }
}
