---
title: 项目配置
order: 3
category:
- 拓展
description: EasyRelation easy-relation 拓展 extension 配置 Configuration
---

项目提供了配置类：`RelationProperties`，会在声明 `InjectRelation` 实例时传入，所以，当需要自定义参数时，也需要将自定义实现传递给 `InjectRelation`。

当使用 SpringBoot 时，还可以在项目配置文件中指定相应的配置。

默认配置如下：

```yaml
easy:
  relation:
    default-exception-strategy: WARN
    redis:
      key-prefix: 'easii:'
      serialize-type: JACKSON
```

### default-exception-strategy

- 描述：默认的转换异常处理策略
- 类型：`RelationExceptionStrategy` 枚举
- 默认值：`THROW`
- 可选值：
  - `IGNORE`：忽略异常
  - `WARN`：打印警告日志
  - `THROW`：抛出异常

在该配置文件中指定的策略，属于系统默认策略，如果在类型配置的关联关系中指定 `exceptionStrategy` 时，则后者优先级更高。

具体示例可以参考[配置关联关系#转换异常](/guide/configure-relation.html#转换异常)

### redis

> 该配置下属于 Redis 插件包中的配置，使用的前提是引入了 `easy-relation-redis` 模块。

#### key-prefix

- 描述：缓存 key 的前缀
- 类型：`String`
- 默认值：`null`

#### serialize-type

- 描述：缓存 value 的序列化方式
- 类型：`RedisSerializeTypeEnum` 枚举
- 默认值：`JACKSON`
- 可选值：
  - `JDK`：使用 JDK 序列化方式
  - `JACKSON`：使用 Jackson 序列化方式

