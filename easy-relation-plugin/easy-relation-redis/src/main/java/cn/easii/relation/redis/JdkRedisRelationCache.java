package cn.easii.relation.redis;

import cn.easii.relation.Constants;
import cn.easii.relation.properties.RelationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = Constants.EasyRelation, name = "redis.serialize-type", havingValue = "jdk", matchIfMissing = true)
public class JdkRedisRelationCache extends AbstractRedisRelationCache {
    public JdkRedisRelationCache(final RedisConnectionFactory redisConnectionFactory,
        RelationProperties relationProperties) {
        super(redisConnectionFactory, relationProperties);
    }

    @Override
    protected RedisSerializer valueSerializer() {
        return new JdkSerializationRedisSerializer();
    }
}
