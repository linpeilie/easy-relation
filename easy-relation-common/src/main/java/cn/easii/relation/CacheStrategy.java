package cn.easii.relation;

public enum CacheStrategy {

    /**
     * 强制开启缓存
     */
    ENABLE,

    /**
     * 默认，默认情况下以 DataProvider 上配置的为准
     */
    DEFAULT,

    /**
     * 禁用缓存
     */
    DISABLE

}
