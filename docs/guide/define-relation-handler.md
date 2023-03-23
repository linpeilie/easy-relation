---
title: 定义数据提供者
order: 2
category:
- 指南
description: EasyRelation 定义数据提供者 DataProvider 指南 Guide
---

## 定义数据提供者「DataProvider」

针对一个数据关联操作，DataProvider 是必须的，这是数据关联的数据提供方，在关联时，会自动调用该方法，获取并解析数据。

数据提供者所在的类要继承 `DataProvideService` 接口，同时在方法上面添加 `@DataProvider` 注解，同时在注解中指定唯一标识。

例如：

关联用户信息的数据提供者：

```java
@Component
public class UserInfoDataProviderHandler implements DataProvideService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        if (StrUtil.isEmpty(req.getUsername())) {
            throw new IllegalArgumentException("username is empty");
        }
        if ("admin".equals(req.getUsername())) {
            final User user = new User();
            user.setUsername("admin");
            user.setNickName("管理员");
            return user;
        }
        return null;
    }

}
```

## 注意事项

### 数据提供者唯一标识不可重复

数据关联时，会通过数据提供者的唯一标识来进行关联查询，所以当前唯一标识不可重复

### 参数类型

数据关联查询方法入参支持的类型如下：

- `String`
- `Integer`
- `Long`
- 自定义的 Java Bean

当参数类型为 `String` / `Integer` / `Long` 时，配置的关联关系必须只有一个条件，且关联字段类型与参数类型相同。可以参考[配置关联关系#兼容单个参数](/guide/configure-relation.html#兼容单个参数)

### 返回参数类型

理论情况下，支持任意的返回类型，但在使用时，需要注意关联关系。

当需要关联的属性，是返回类型中的某个字段，需要在关联关系中通过 `targetField` 属性指定当前字段名称，例如[配置关联关系#关联目标类型属性-平铺关联](/guide/configure-relation.html#关联目标类型属性-平铺关联)中的示例；
当需要关联的类型与该方法提供的类型一致，则不需要指定 `targetField` 字段。

例如，上面的示例中，用户信息提供者返回的类型为 `User`，这里只需要关联其昵称，则需要在 `@Relation#targetField` 指定为 `nickName`。
如果另外提供了一个方法，只提供用户昵称信息，返回类型为 `String`，则不需要指定 `targetField`，例如如下定义：

```java
@Component
public class UserInfoDataProviderHandler implements DataProvideService {

    @DataProvider(RelationIdentifiers.getNickNameByUsername)
    public String getNickNameByUsername(String username) {
        if ("admin".equals(username)) {
            return "管理员";
        }
        return null;
    }

}
```

### 注册到 DataProviderRepository

当想要在框架中使用定义的数据提供者时，需要提前在 `DataProviderRepository` 中调用 `registerProvider` 方法注册该对象。

如果在 `SpringBoot` 环境下使用，则只需要声明为当前类为 Spring Bean 即可，在项目启动阶段，会自动注册。