package cn.easii.relation;

public enum RelationExceptionStrategy {

    /**
     * 使用配置的默认值
     */
    DEFAULT,

    /**
     * 忽略异常
     */
    IGNORE,

    /**
     * 打印警告日志
     */
    WARN,

    /**
     * 抛出异常
     */
    THROW;

}
