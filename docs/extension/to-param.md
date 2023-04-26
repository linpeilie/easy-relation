---
title: 关联查询参数生成策略
order: 2
category:
- 拓展
description: EasyRelation easy-relation 拓展 extension 策略 ToParam 关联查询参数生成策略
---

在关联操作时，首先会把根据配置的关联关系，将所有的参数添加到 `Map<String, Object>` 中，在调用实际方法时，
再将 `Map<String, Object>` 转换为关联查询方法的参数类型实例。针对于该步骤，提供了相应的拓展接口。

> 这里需要注意，为了便捷和性能，当关联查询的方法入参类型为 `String`、`Integer`、`Long`  中的任意一种时，
> 支持直接不进行额外的转换操作，直接将关联的具体值，赋值给关联查询方法。可以参考[配置关联关系#兼容单个参数](/guide/configure-relation.html#兼容单个参数)

## 默认实现

默认情况下会使用 JSON 的形式进行转换，即先将 `Map<String, Object>` 转换为 JSON 字符串，之后再转换为具体入参对象。

## 自定义实现

如果想要自定义该实现时，需要继承 `AbstractMapToBeanHandle`，实现其 `<P> P toBean(final Map<String, Object> map, final Class<P> clazz)` 方法。
之后在声明 `InjectRelation` 实例时，传入该自定义类型即可。

如果 SpringBoot 运行环境下，可以将该自定义实现声明为 Spring 的 Bean 对象，在项目启动阶段，会自动注入。