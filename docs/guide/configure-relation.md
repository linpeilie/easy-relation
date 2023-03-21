---
title: 配置关联关系
order: 1
category:
- 指南
description: EasyRelation 配置关联关系 ConfigureRelation 指南
---

## 基础配置

当类型中，有需要关联查询获取的字段时，可以通过 `@Relation` 注解配置关联关系。
例如：

```java

@Data
public class User {

    private String username;

    private String nickName;

    @Relation(handler = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

}
```

上面的例子中，表示 `User` 中的 `role` 属性，需要通过唯一标识为 `getRoleByUsername`
的关联处理器（被 `@RelationHandler` 注解标注的方法）来获取，且使用当前对象中的 `username` 属性作为关联条件。

