# EasyRelation

## 这是什么？

EasyRelation 是一个简单、高效的自动关联数据框架，可以通过一行代码，自动关联查询并填充需要的数据，对于性能影响极小，且省略了大量冗余代码。

该项目适应于**当前对象中的字段需要关联查询，并赋值到当前对象中**，数据来源可以是**枚举**、**数据库**、**RPC接口** 等等任意来源。

**如果该项目帮助了您，希望能点个 Start 鼓励一下！**

## 特点

- 不限制关联查询方式，需要关联的数据可以是任意来源
- 两级缓存支持，可自由选择使用的缓存
- 执行效率高，对性能影响极小

## 快速开始

### 安装依赖

- maven

```xml
<dependency>
    <groupId>cn.easii</groupId>
    <artifactId>easy-relation-spring-boot-starter</artifactId>
    <version>${easy-relation.version}</version>
</dependency>
```

### 定义对象

假设有一个订单模型（`Order`），其只保存了用户名，需要关联查询昵称：

```java
@Data
public class Order {

    private String orderId;

    private String username;

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName",
        condition = {@Condition(field = "username")})
    private String nickName;

}
```

如上定义中，在需要关联查询的字段，添加`@Relation`注解，指定关联关系，这里的 `targetField` 表示当前字段需要查询结果中的指定 `nickName` 属性。

### 定义数据关联查询处理器

定义一个类，继承 `RelationService`，且实现一个查询用户信息的方法，添加 `@RelationHandler` 注解，并指定其唯一标识，

```java
@Component
public class UserInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        // 这里可以从任意来源获取值
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

### 使用

```java
@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    @Test
    void quickStart() {
        Order order = new Order();
        order.setOrderId("2f453910375641648ab3a2fc6e3328ef");
        order.setUsername("admin");
        injectRelation.injectRelation(order);
        System.out.println(order);  // Order(orderId=2f453910375641648ab3a2fc6e3328ef, username=admin, nickName=管理员)
        Assert.equals(order.getNickName(), "管理员");
    }

}
```

### 总结

使用 EasyRelation 主要有三步：
1. 在类结构中配置关联关系
2. 定义关联查询数据源
3. 获取 `InjectRelation` 实例，调用其 `injectRelation` 方法，自动注入关联数据

## 联系我

> vx : Clue8a796d01

<img src="https://raw.githubusercontent.com/linpeilie/mapstruct-plus/main/assets/contact-me.jpeg" alt="联系我" width="364" height="497" />