---
title: 缓存
order: 1
category:
- 拓展
description: EasyRelation easy-relation 拓展 extension 缓存 Cache 二级缓存
---

## 缓存分层

EasyRelation 中共设计有两级缓存，参考了 Mybatis 中的设计，在进行数据关联时，会依次经过 `一级缓存 --> 二级缓存 ---> 数据提供源`，从而提高数据获取的效率。

这里简单了解一下一级缓存与二级缓存：

- **一级缓存**：单次数据关联操作内的缓存，缓存的数据只在这个关联过程内有效，一级缓存根据一定规则，会自动开启。
> 这里的单次数据关联操作指的是调用一次 `injectRelation` 方法内的执行流程。
- **二级缓存**：全局缓存，比如使用 Redis 作为缓存，二级缓存需要手动开启。

## 一级缓存

### 为什么需要一级缓存

在进行数据关联时，在对一个集合进行关联，或者一个对象内有[平铺关联](/guide/configure-relation.html#关联目标类型属性-平铺关联)的操作时，
针对同一个数据提供者，且条件相同时，每一次关联都查询一次数据库，或者调用一次 Rpc 接口，会白白浪费系统的资源，并且还会导致类似于数据库中的脏读、幻读、不可重复读等问题。

为了解决这个问题，EasyRelation 会在执行时，进行判断是否需要一级缓存，当需要时，会自动打开缓存，缓存条件和相应的结果，从而保证性能和同一次关联操作中的一致性问题。

### 一级缓存实现

当一级缓存开启状态时，会先到缓存中查询是否有，如果没有，则经过二级缓存、数据提供者，获取数据，最终存储到一级缓存中。

一级缓存是在当前线程的 `ThreadLocal` 中绑定了一个 Map 对象，执行前根据一定规则开启，执行后，会自动清除。

### 一级缓存的开启条件

为了进一步节省性能，一级缓存并不会在每一次关联操作中开启，只会在判断需要的时候才会开启。

目前当下面两种情况时，会自动开启：

1. 当一次对一个集合中的元素进行关联时；
2. 当在一个对象中出现[平铺关联](/guide/configure-relation.html#关联目标类型属性-平铺关联)的操作时；

即，EasyRelation 会判断不会对同一个数据提供源进行多次调用时，则不会开启一级缓存。

## 二级缓存

二级缓存主要是为了全局缓存，当没有命中一级缓存时，首先会到二级缓存中查找，如果找到相应的值则直接解析返回。

### 二级缓存的实现

框架中提供了一个二级缓存的实现，实现比较简单，因为一般项目中也会用 Redis 来实现缓存。

针对二级缓存定义了一个接口 —— `RelationCache`，接口定义如下：

```java
public interface RelationCache {

    /**
     * 指定键值是否存在
     *
     * @param key 键
     * @return 指定键值是否存在
     */
    boolean hasKey(String key);

    /**
     * 保存缓存
     *
     * @param key   键值
     * @param value 实际值
     * @param time  过期时间，单位：ms
     */
    void set(String key, Object value, long time);

    /**
     * 获取缓存数据
     *
     * @param key 键值
     * @return {@link Object} key对应的值
     */
    Object get(String key);

}
```

#### 默认实现

框架中的默认实现，是基于两个 `Map` 和 `Tasker` 来实现的。

保存一个 KV 时，会同时保存其过期时间，通过 `Tasker` 来过期 key。

#### 集成 Redis 作为 Key

EasyRelation 可以提供了 Redis 缓存的插件包，其中定义了 JDK、Jackson 两种 value 序列化方式。

具体使用可以参考[Redis 缓存](/plugins/redis-cache)

#### 自定义缓存

当要实现一个二级缓存时，需要实现该接口，并且声明 `InjectRelation` 对象时，传入该实现类。

如果使用 SpringBoot，将自定义缓存的实现类声明为一个 Spring 的 Bean 即可。

### 二级缓存的开启

二级缓存**默认是禁用状态，需要手动开启**。

首先，在配置 `DataProvider` 时，可以通过 `useCache` 配置当前数据提供者的默认缓存开关配置，当前配置只有在配置 `@Relation` 时，
`cacheStrategy` 为 `DEFAULT` 时才会生效，详情可以参考[配置关联关系#启用缓存](/guide/configure-relation.html#启用缓存)

同样还支持每个关联类型内部的个性化配置，比如通过 `@Relation` 中的 `cacheStrategy` 配置强制开启缓存或强制关闭缓存。

当通过该属性配置强制开启/关闭时，会忽略 `DataProvider` 中的缓存开关配置。详情可以参考[配置关联关系#启用缓存](/guide/configure-relation.html#启用缓存)

## 缓存 Key 的生成规则

`数据提供者唯一标识` + `-` + 参数列表（`参数名称` + `-` + `参数值`）。

例如：

`getUserByUsername-username-admin`

