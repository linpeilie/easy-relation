package cn.easii.relation.properties;

import cn.easii.relation.RelationExceptionStrategy;
import lombok.Data;

@Data
public class RelationProperties {

    /**
     * 默认异常处理策略
     */
    private RelationExceptionStrategy defaultExceptionStrategy = RelationExceptionStrategy.THROW;

    /**
     * 启用 Redis 缓存时的配置
     */
    private RelationRedisProperties redis = new RelationRedisProperties();

}
