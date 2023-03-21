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

## 关联条件配置

`@Relation` 注解中，可以通过 `condition` 注解，来配置关联条件，该属性支持配置多个关联条件。

在配置关联条件时，必须要在 `@Condition#field` 中指定使用当前类型中的哪个属性来关联。

在 `@Condition` 注解中，还有一个参数：`paramField`，该参数表示在关联查询时，当前对象中的关联属性，与关联查询对象中属性的对应关系。
即，在关联查询时，当前属性的值，需要赋值给入参对象中的哪个字段。
如果没有配置该属性的话，默认取 `field` 配置的名称。

例如：

基于[快速开始](/introduction/quick-start.html)中的示例，用户信息查询时需要 `UserQueryReq` 类型的参数，
当 `Order` 对象关联用户昵称时，会将当前订单中的 `username` 赋值给 `UserQueryReq` 中的 `username` 来进行查询。

## 多关联条件

上一节讲过可以通过配置多个 `@Condition` 来指定多个关联条件来查询，这里只做一个演示。

基于[快速开始](/introduction/quick-start.html)中的示例，`Order` 和 `UserQueryReq` 类中增加 `source` 字段，且关联查询用户昵称时，需要同时使用该字段，代码如下：

::: code-tabs#java
@tab Order
```java
@Data
public class Order {

    private String source;

    private String orderId;

    private String username;

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName",
        condition = {
            @Condition(field = "username"),
            @Condition(field = "source")
        })
    private String nickName;

}
```
@tab UserQueryReq
```java
@Data
@AutoMapMapper
public class UserQueryReq {

    private String username;

    private Long userId;

    private Boolean isDeleted;

    private String source;

}
```
@tab UserInfoRelationHandler
```java
@Component
public class UserInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        System.out.println("req = " + req);
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
@tab QuickStart
```java
public class QuickStart {

    private InjectRelation injectRelation;

    @BeforeEach
    public void before() {
        // 注册用户信息获取接口
        RelationHandlerRepository.registerHandler(new UserInfoRelationHandler());
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
        order.setSource("ODD");
        order.setUsername("admin");
        return order;
    }

}
```
:::

执行 `QuickStart` 时，控制台会打印如下信息：

```
req = UserQueryReq(username=admin, userId=null, isDeleted=null, source=ODD)
Order(source=ODD, orderId=eb35e18caa552284b39d427c1e06f9f7, username=admin, nickName=管理员)
```

## 常量条件

有的时候，关联查询需要带一些固定的条件，例如：用户关联查询权限时，只能查询启用状态下的权限列表。

当这种情况时，可以通过 `@Realtion#constantsCondion` 来配置这种固定的条件。

例如：

::: code-tabs#java
@tab User
```java
@Data
public class User {

    private String username;

    @Relation(handler = RelationIdentifiers.getPermissionsByUsername, condition = {
        @Condition(field = "username")}, constantsCondition = {@ConstantsCondition(field = "state", value = "1")})
    private List<Permission> permissions;
    
}
```

@tab PermissionQueryReq
```java
@Data
public class PermissionQueryReq {

    private String username;

    private Integer state;

}
```

@tab PermissionRelationHandler
```java
@Component
public class PermissionRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getPermissionsByUsername)
    public List<Permission> getPermissionsByUsername(PermissionQueryReq permissionQueryReq) {
        System.out.println("permissionQueryReq:" + permissionQueryReq);
        if (!Constants.ENABLED.equals(permissionQueryReq.getState())) {
            return Collections.emptyList();
        }
        if ("admin".equals(permissionQueryReq.getUsername())) {
            List<Permission> permissions = new ArrayList<>();
            final Permission permission1 = new Permission();
            permission1.setResource("/user");
            final Permission permission2 = new Permission();
            permission2.setResource("/role");
            final Permission permission3 = new Permission();
            permission3.setResource("/permission");
            permissions.add(permission1);
            permissions.add(permission2);
            permissions.add(permission3);
            return permissions;
        }
        return Collections.emptyList();
    }

}
```

@tab InjectRelationTest
```java
@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    private User initUser() {
        User user = new User();
        user.setUsername("admin");
        user.setNickName("管理员");
        user.setCreateUsername("admin");
        return user;
    }

    @Test
    void injectRelation() {
        final User user = initUser();
        injectRelation.injectRelation(user);
        System.out.println(user);
    }

}
```
:::

控制台打印如下：

```
permissionQueryReq:PermissionQueryReq(username=admin, state=1)
User(username=admin, nickName=管理员, icon=null, role=Role(roleId=100001, roleName=系统管理员), permissions=[Permission(resource=/user), Permission(resource=/role), Permission(resource=/permission)], createUsername=admin, createNickName=管理员)
```

## 兼容单个参数

有的时候，在关联查询时，只有一个参数即可查出，不想因此建一个查询入参对象。框架针对这种情况做了特殊处理。

当满足以下条件：
1. 关联处理器方法入参只有一个，且类型为 `String`、`Integer`、`Long` 任意一种；
2. 类型中配置的关联关系只有一个关联条件。

例如：用户（`User`）模型中需要关联角色（`Role`），这里只想根据 `username` 来查询相应的角色信息。

::: code-tabs#java
@tab User
```java
@Data
public class User {

    private String username;

    @Relation(handler = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

}
```

@tab RoleInfoRelationHandler
```java
@Component
public class RoleInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getRoleByUsername)
    public Role getRoleByUsername(String username) {
        if ("admin".equals(username)) {
            final Role role = new Role();
            role.setRoleId("100001");
            role.setRoleName("系统管理员");
            return role;
        }
        return null;
    }

}
```

@tab 测试
```java
@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    private User initUser() {
        User user = new User();
        user.setUsername("admin");
        user.setNickName("管理员");
        user.setCreateUsername("admin");
        return user;
    }

    @Test
    void injectRelation() {
        final User user = initUser();
        injectRelation.injectRelation(user);
        System.out.println(user);   // User(username=admin, role=Role(roleId=100001, roleName=系统管理员))
    }

}
```
:::

