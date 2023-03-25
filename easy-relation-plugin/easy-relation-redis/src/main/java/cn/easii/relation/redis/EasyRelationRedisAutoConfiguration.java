package cn.easii.relation.redis;

import cn.easii.relation.Constants;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class EasyRelationRedisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(RelationRedisProperties.class)
    @ConfigurationProperties(prefix = Constants.EasyRelation + ".redis")
    public RelationRedisProperties relationRedisProperties() {
        return new RelationRedisProperties();
    }

    @Bean
    @ConditionalOnProperty(prefix = Constants.EasyRelation, name = "redis.serialize-type", havingValue = "jackson")
    public JacksonRedisRelationCache jacksonRedisRelationCache(RedisConnectionFactory redisConnectionFactory,
        RelationRedisProperties redisProperties) {
        return new JacksonRedisRelationCache(redisConnectionFactory, redisProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = Constants.EasyRelation, name = "redis.serialize-type", havingValue = "jdk")
    public JdkRedisRelationCache jdkRedisRelationCache(RedisConnectionFactory redisConnectionFactory,
        RelationRedisProperties redisProperties) {
        return new JdkRedisRelationCache(redisConnectionFactory, redisProperties);
    }

}
