---
title: 数据关联
order: 3
category:
- 指南
- description: EasyRelation easy-relation 指南 guide 数据关联 数据关联API InjectRelation
---

## InjectRelation

当配置好关联关系和数据提供者时，就可以通过 `InjectRelation` 提供的方法，在需要的时候进行数据关联了。

当需要数据关联时，需要先获取 `InjectRelation` 实例（例如，可以用 Spring 注入的方式），再调用相应的方法来进行数据关联。

其内部提供了两个方法：

- `public <T> void injectRelation(T t, String... relationFields)`
- `public <T> void injectRelation(List<T> list, String... relationFields)`

这两个方法分别针对单个对象和集合来进行关联。

> 这里需要注意，在处理集合时，会默认开启一级缓存（参考[缓存](/extension/cache.html)），所以当集合时，建议调用第二个方法。

第二个参数是指定要关联的属性名称，当不指定时，默认关联配置的所有需要关联的属性。

示例：

```java
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

    @Test
    public void injectSingleField() {
        final User user = initUser();
        injectRelation.injectRelation(user, "createNickName");
        System.out.println(user);
        Assert.isNull(user.getRole());
        Assert.isNull(user.getPermissions());
    }

    @Test
    public void injectList() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(initUser());
        }
        injectRelation.injectRelation(userList);
        for (User user : userList) {
            Assert.notNull(user.getRole());
            Assert.notNull(user.getPermissions());
            Assert.notNull(user.getCreateNickName());
        }
    }

    @Test
    public void injectListSingleField() {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            userList.add(initUser());
        }
        injectRelation.injectRelation(userList, "createNickName");
        for (User user : userList) {
            Assert.isNull(user.getRole());
            Assert.isNull(user.getPermissions());
            Assert.notNull(user.getCreateNickName());
        }
    }

}
```