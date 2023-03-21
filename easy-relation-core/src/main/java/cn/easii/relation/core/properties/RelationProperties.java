package cn.easii.relation.core.properties;

import cn.easii.relation.annotation.RelationExceptionStrategy;
import lombok.Data;

@Data
public class RelationProperties {

    /**
     * 默认异常处理策略
     */
    private RelationExceptionStrategy defaultExceptionStrategy = RelationExceptionStrategy.THROW;

}
