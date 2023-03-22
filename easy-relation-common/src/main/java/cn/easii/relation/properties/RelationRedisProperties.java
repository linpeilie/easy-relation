package cn.easii.relation.properties;

import cn.easii.relation.RedisSerializeTypeEnum;
import lombok.Data;

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
