package cn.easii.relation.redis;

import cn.easii.relation.Constants;
import cn.easii.relation.RedisSerializeTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
public class RelationRedisProperties {

    /**
     * 序列化方式：支持两种
     */
    private RedisSerializeTypeEnum serializeType = RedisSerializeTypeEnum.JACKSON;

    /**
     * 缓存 key 前缀
     */
    private String keyPrefix;

}
