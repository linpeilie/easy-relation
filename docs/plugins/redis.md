---
title: 集成Redis
order: 2
category:
- 插件
description: EasyRelation easy-relation 插件 Plugins plugins redis cache
---

如果想使用 Redis 作为二级缓存的话，EasyRelation 提供了便捷的 Redis 集成插件包。

## 安装依赖

```xml
<dependency>
    <groupId>cn.easii</groupId>
    <artifactId>easy-relation-redis</artifactId>
    <version>最新版本</version>
</dependency>
```

当需要使用 Jackson 序列化方式时，还需要添加 `jackson-databind` 依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
```

## 配置项

默认配置如下：

```yaml
easy:
  relation:
    redis:
      key-prefix: 'easii:'
      serialize-type: jackson
```

#### key-prefix

- 描述：缓存 key 的前缀
- 类型：`String`
- 默认值：`null`

#### serialize-type

- 描述：缓存 value 的序列化方式
- 类型：`RedisSerializeTypeEnum` 枚举
- 默认值：`JACKSON`
- 可选值：
  - `jdk`：使用 JDK 序列化方式
  - `jackson`：使用 Jackson 序列化方式
