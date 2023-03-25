package cn.easii.relation.properties;

import cn.easii.relation.RelationExceptionStrategy;
import lombok.Data;

@Data
public class RelationProperties {

    /**
     * 默认异常处理策略
     */
    private RelationExceptionStrategy defaultExceptionStrategy = RelationExceptionStrategy.THROW;

}
