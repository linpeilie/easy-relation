---
title: 配置关联关系
order: 1
category:
- 指南
description: EasyRelation 配置关联关系 ConfigureRelation 指南 Guide
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
的数据提供者（被 `@DataProvider` 注解标注的方法）来获取，且使用当前对象中的 `username` 属性作为关联条件。

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
@tab UserInfoDataProvider
```java
@Component
public class UserInfoDataProvider implements DataProviderService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
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

@tab PermissionDataProvider
```java
@Component
public class PermissionDataProvider implements DataProviderService {

    @DataProvider(RelationIdentifiers.getPermissionsByUsername)
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
1. 数据提供者方法入参只有一个，且类型为 `String`、`Integer`、`Long` 任意一种；
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

@tab RoleInfoDataProvider
```java
@Component
public class RoleInfoDataProvider implements DataProviderService {

    @DataProvider(RelationIdentifiers.getRoleByUsername)
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

## 关联目标类型属性/平铺关联

有的时候，需要关联另一个模型中的部分属性，并不需要关联所有属性，但复用的关联数据查询接口，返回的是整个对象模型。
针对这种场景，在 `@Relation` 中提供了一个配置 —— `targetField`，该属性可以配置，获取到关联对象的具体属性值。

例如[快速开始](/introduction/quick-start.html)中，订单模型中需要根据 `username` 关联查询 `nickName`，
但提供的数据提供者返回的是 `User` 类，所以这里需要指定 `targetField` 为 `User#nickName`。

这里需要注意，当需要从一个对象模型中关联多个对象时，并不会查询多次。例如，需要关联用户昵称和用户头像，只能查询一次。
详情可以查看[缓存](/guide/cache.html)。

## 启用缓存

针对于每一个类型中的每个关联属性，都可以单独配置是否启用缓存。

`@Relation` 注解中提供了两个属性：`useCache`、`cacheTimeout`。
分别用来配置是否启用缓存，和缓存的失效时间「单位：s」。

> 这里需要注意：注解中配置的缓存是指 EasyRelation 中的定义的二级缓存

具体可参考[缓存](/guide/cache.html)

下面是一个使用缓存的例子：

定义一个文章类 `Article`，其中保存了作者用户名，需要关联作者昵称.

::: code-tabs#java
@tab Article 
```java
@Data
public class Article {

    private String content;

    private String authorUsername;

    @Relation(handler = RelationIdentifiers.getUserByUsername, condition = {
        @Condition(field = "authorUsername", paramField = "username")}, targetField = "nickName",
        useCache = true, cacheTimeout = 5)
    private String nickName;

}
```

@tab UserInfoDataProvider
```java
public class UserInfoDataProvider implements DataProviderService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        System.out.println(DateUtil.now() + "req : " + req);
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

@tab CacheTest
```java
public class CacheTest {

    private InjectRelation injectRelation;

    private UserInfoDataProvider userInfoDataProvider;

    @BeforeEach
    public void before() {
        injectRelation = new InjectRelation();
        userInfoDataProvider = new UserInfoDataProvider();
        if (DataProviderRepository.getHandler(RelationIdentifiers.getUserByUsername) == null) {
            DataProviderRepository.registerHandler(userInfoDataProvider);
        }
    }

    @Test
    public void test() throws InterruptedException {
        Article article = getArticle("admin");
        injectRelation.injectRelation(article);
        System.out.println(article);
        Assert.equals(article.getNickName(), "管理员");
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            article.setNickName(null);
            injectRelation.injectRelation(article);
        }
        Assert.equals(article.getNickName(), "管理员");
    }

    public Article getArticle(String username) {
        final Article article = new Article();
        article.setContent("EasyRelation是一个简单、快速、强大的数据自动关联框架");
        article.setAuthorUsername(username);
        return article;
    }

}
```
:::

控制台输出如下：

```
2023-03-21 21:39:46req : UserQueryReq(username=admin, userId=null, isDeleted=null)
Article(content=EasyRelation是一个简单、快速、强大的数据自动关联框架, authorUsername=admin, nickName=管理员)
2023-03-21 21:39:51req : UserQueryReq(username=admin, userId=null, isDeleted=null)
```

## 异常抛出

在关联时，如果出现异常，可以手动配置是否将该异常抛出。

`@Relation` 注解中提供了 `exceptionStrategy` 属性，用来配置关联数据发生异常时的处理策略，默认由 `RelationProperties` 指定（当前类中的默认值为抛出异常）。

基于前面的示例，在 `UserInfoDataProvider` 中增加判断，当传入 `username` 为 `null` 时，则抛出异常：

```java
@DataProvider(RelationIdentifiers.getUserByUsername)
public User getUserByUsername(UserQueryReq req) {
    System.out.println(DateUtil.now() + "req : " + req);
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
```

添加测试用例：

```java
@Test
void testThrowException() {
    final Article article = getArticle(null);
    injectRelation.injectRelation(article);
}
```

执行后，控制台打印如下：

::: details 控制台打印信息
```
2023-03-21 22:26:28req : UserQueryReq(username=null, userId=null, isDeleted=null)

cn.easii.relation.exception.RelationException: java.lang.reflect.InvocationTargetException

	at cn.easii.relation.core.InjectRelation.handleException(InjectRelation.java:105)
	at cn.easii.relation.core.InjectRelation.handleException(InjectRelation.java:108)
	at cn.easii.relation.core.InjectRelation.injectOn(InjectRelation.java:90)
	at cn.easii.relation.core.InjectRelation.lambda$injectRelation$0(InjectRelation.java:58)
	at cn.easii.relation.core.InjectRelation.cachedInvoke(InjectRelation.java:68)
	at cn.easii.relation.core.InjectRelation.injectRelation(InjectRelation.java:58)
	at cn.easii.relation.core.ArticleTest.testThrowException(ArticleTest.java:56)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:156)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:147)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:86)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(InterceptingExecutableInvoker.java:103)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.lambda$invoke$0(InterceptingExecutableInvoker.java:93)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:92)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:86)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.lambda$invokeTestMethod$7(TestMethodTestDescriptor.java:217)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.invokeTestMethod(TestMethodTestDescriptor.java:213)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:138)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor.execute(TestMethodTestDescriptor.java:68)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:151)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.invokeAll(SameThreadHierarchicalTestExecutorService.java:41)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$6(NodeTestTask.java:155)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$8(NodeTestTask.java:141)
	at org.junit.platform.engine.support.hierarchical.Node.around(Node.java:137)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.lambda$executeRecursively$9(NodeTestTask.java:139)
	at org.junit.platform.engine.support.hierarchical.ThrowableCollector.execute(ThrowableCollector.java:73)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.executeRecursively(NodeTestTask.java:138)
	at org.junit.platform.engine.support.hierarchical.NodeTestTask.execute(NodeTestTask.java:95)
	at org.junit.platform.engine.support.hierarchical.SameThreadHierarchicalTestExecutorService.submit(SameThreadHierarchicalTestExecutorService.java:35)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestExecutor.execute(HierarchicalTestExecutor.java:57)
	at org.junit.platform.engine.support.hierarchical.HierarchicalTestEngine.execute(HierarchicalTestEngine.java:54)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:147)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:127)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:90)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.lambda$execute$0(EngineExecutionOrchestrator.java:55)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.withInterceptedStreams(EngineExecutionOrchestrator.java:102)
	at org.junit.platform.launcher.core.EngineExecutionOrchestrator.execute(EngineExecutionOrchestrator.java:54)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:114)
	at org.junit.platform.launcher.core.DefaultLauncher.execute(DefaultLauncher.java:86)
	at org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher.execute(DefaultLauncherSession.java:86)
	at org.junit.platform.launcher.core.SessionPerRequestLauncher.execute(SessionPerRequestLauncher.java:53)
	at com.intellij.junit5.JUnit5IdeaTestRunner.startRunnerWithArgs(JUnit5IdeaTestRunner.java:57)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater$1.execute(IdeaTestRunner.java:38)
	at com.intellij.rt.execution.junit.TestsRepeater.repeat(TestsRepeater.java:11)
	at com.intellij.rt.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:35)
	at com.intellij.rt.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:235)
	at com.intellij.rt.junit.JUnitStarter.main(JUnitStarter.java:54)
Caused by: java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at cn.easii.relation.core.DataProviderRepository.lambda$registerHandler$0(DataProviderRepository.java:28)
	at cn.easii.relation.core.InjectRelation.inject(InjectRelation.java:152)
	at cn.easii.relation.core.InjectRelation.injectOn(InjectRelation.java:88)
	... 74 more
Caused by: java.lang.IllegalArgumentException: username is empty
	at cn.easii.relation.core.handler.UserInfoDataProvider.getUserByUsername(UserInfoDataProvider.java:17)
	... 81 more
```
:::

当使用 SpringBoot 环境时，可以在配置文件中配置该默认值，例如可以指定为警告：

```yaml
easy:
  relation:
    default-exception-strategy: WARN
```

在 SpringBoot 环境中执行测试方法，控制台打印如下：

::: details 控制台打印信息
```java
req = UserQueryReq(username=null, userId=null, isDeleted=null, source=null)
2023-03-21T22:36:33.243+08:00  WARN 14981 --- [           main] cn.easii.relation.core.InjectRelation    : an exception occurred while getting the relation data, error info : java.lang.reflect.InvocationTargetException
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at cn.easii.relation.core.DataProviderRepository.lambda$registerHandler$0(DataProviderRepository.java:28)
	at cn.easii.relation.core.InjectRelation.inject(InjectRelation.java:152)
	at cn.easii.relation.core.InjectRelation.injectOn(InjectRelation.java:88)
	at cn.easii.relation.core.InjectRelation.lambda$injectRelation$0(InjectRelation.java:58)
	at cn.easii.relation.core.InjectRelation.cachedInvoke(InjectRelation.java:68)
	at cn.easii.relation.core.InjectRelation.injectRelation(InjectRelation.java:58)
	at cn.easii.example.ArticleTest.testThrowException(ArticleTest.java:47)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.junit.platform.commons.util.ReflectionUtils.invokeMethod(ReflectionUtils.java:727)
	at org.junit.jupiter.engine.execution.MethodInvocation.proceed(MethodInvocation.java:60)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$ValidatingInvocation.proceed(InvocationInterceptorChain.java:131)
	at org.junit.jupiter.engine.extension.TimeoutExtension.intercept(TimeoutExtension.java:156)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestableMethod(TimeoutExtension.java:147)
	at org.junit.jupiter.engine.extension.TimeoutExtension.interceptTestMethod(TimeoutExtension.java:86)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker$ReflectiveInterceptorCall.lambda$ofVoidMethod$0(InterceptingExecutableInvoker.java:103)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.lambda$invoke$0(InterceptingExecutableInvoker.java:93)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain$InterceptedInvocation.proceed(InvocationInterceptorChain.java:106)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.proceed(InvocationInterceptorChain.java:64)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.chainAndInvoke(InvocationInterceptorChain.java:45)
	at org.junit.jupiter.engine.execution.InvocationInterceptorChain.invoke(InvocationInterceptorChain.java:37)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:92)
	at org.junit.jupiter.engine.execution.InterceptingExecutableInvoker.invoke(InterceptingExecutableInvoker.java:86)
	at org.junit.jupiter.engine.descriptor.TestMethodTestDescriptor
```
:::
