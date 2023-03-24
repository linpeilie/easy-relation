---
title: 快速开始
order: 3
category:
- 介绍
tag:
- 快速开始
description: EasyRelation easy-relation 介绍 introduction 快速开始 QuickStart
---

下面演示如何使用 EasyRelation 进行自动关联数据

假设有订单类（`Order`）和用户类（`User`），订单中保存了用户名，需要关联查询用户昵称。

::: code-tabs#java
@tab Order
```java
@Data
public class Order {

    private String orderId;

    private String username;

    private String nickName;

}
```

@tab User
```java
@Data
public class User {
    private String username;
    private String nickName;
}
```
:::

## SpringBoot

### 添加依赖

```xml
<properties>
    <easy-relation.version>最新版本</easy-relation.version>
</properties>
<dependencies>
    <dependency>
        <groupId>cn.easii</groupId>
        <artifactId>easy-relation-spring-boot-starter</artifactId>
        <version>${easy-relation.version}</version>
    </dependency>
</dependencies>
```

### 配置关联关系

在 `Order` 中指定其 `nickName` 属性需要关联查询而来，通过当前类中的 `username` 属性关联，
这里假设查询结果是一个对象，则获取对象中的 `nickName` 属性：

```java
@Data
public class Order {

    // ......

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName",
        condition = {@Condition(field = "username")})
    private String nickName;

}
```

### 定义用户数据数据提供者

这里需要定义一个类，实现 `DataProvideService` 接口，在其中定义获取用户信息的接口，并添加 `@DataProvider` 注解。

```java
@Component
public class UserInfoDataProvider implements DataProvideService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
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

这里的 `UserQueryReq` 为用户信息查询入参，定义如下：

```java
@Data
@AutoMapMapper
public class UserQueryReq {

    private String username;

    private Long userId;

    private Boolean isDeleted;

}
```

### 测试

```java
@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    @Test
    void quickStart() {
        Order order = getOrder("2f453910375641648ab3a2fc6e3328ef");
        injectRelation.injectRelation(order);
        System.out.println(order);  // Order(orderId=2f453910375641648ab3a2fc6e3328ef, username=admin, nickName=管理员)
        Assert.equals(order.getNickName(), "管理员");
    }

    private Order getOrder(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUsername("admin");
        return order;
    }

}
```

## 非 SpringBoot

### 添加依赖

```xml
<properties>
    <easy-relation.version>最新版本</easy-relation.version>
</properties>
<dependencies>
    <dependency>
        <groupId>cn.easii</groupId>
        <artifactId>easy-relation-core</artifactId>
        <version>${easy-relation.version}</version>
    </dependency>
</dependencies>
```

### 配置关联关系

同[SpringBoot 环境下配置关联关系](#配置关联关系)

### 定义用户数据数据提供者

同[SpringBoot 环境下定义用户数据数据提供者](#定义用户数据数据提供者)

### 测试

```java
public class QuickStart {

    private InjectRelation injectRelation;

    @BeforeEach
    public void before() {
        // 注册用户信息获取接口
        DataProviderRepository.registerHandler(new UserInfoDataProvider());
        injectRelation = new InjectRelation();
    }

    @Test
    public void quickStart() {
        // 获取 order 信息
        final Order order = getOrder("eb35e18caa552284b39d427c1e06f9f7");
        injectRelation.injectRelation(order);
        System.out.println(order);  // Order(orderId=eb35e18caa552284b39d427c1e06f9f7, username=admin, nickName=管理员)
        Assert.equals(order.getNickName(), "管理员");
    }

    private Order getOrder(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUsername("admin");
        return order;
    }

}
```

## 总结

使用 EasyRelation 时，基本分为三步：

1. 在对象中使用 `@Relation` 定义关联关系
2. 定义关联数据获取接口，在方法上添加 `@DataProvider`，非 SpringBoot 环境下，还需要手动注入当前接口的实例到 `DataProviderRepository`
3. 获取 `InjectRelation` 实例，调用 `injectRelation` 方法，自动实现关联数据注入

