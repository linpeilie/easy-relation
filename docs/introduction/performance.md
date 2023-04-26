---
title: 性能
order: 5
category:
- 介绍
description: EasyRelation easy-relation 介绍 introduction 性能 performance
---

这里假设一个常见的例子：根据用户名关联获取用户昵称，关联条件只有一个，根据关联查询方法的入参和出参分别提供了四种场景测试：

- **场景一**：关联查询时查询方法的入参和出参类型一致，框架中不需要类型转换。

  例如：根据用户名获取用户昵称

  ```java
  public String getNickNameByUsername(String username) {
      if (admin.equals(username)) {
          return nickName;
      }
      return null;
  }
  ```

- **场景二**：关联查询时查询方法的入参类型一致，出参类型不一致，框架中需要解析返回结果。同场景一的例子如下：

  ```java
  public User getUserByUsername(String username) {
      if (admin.equals(username)) {
          return user;
      }
      return null;
  }
  ```

- **场景三**：关联查询时查询方法的入参类型不一致，出参类型一致，框架中需要解析入参类型。方法示例如下：

  ```java
  public String getNickname(UserQueryReq userQueryReq) {
      if (admin.equals(userQueryReq.getUsername())) {
          return nickName;
      }
      return null;
  }
  ```

- **场景四**：关联查询时查询方法的入参和出参类型都不一致，框架中需要解析入参和出参类型。方法示例如下：

  ```java
  public User getUser(UserQueryReq userQueryReq) {
      if (admin.equals(userQueryReq.getUsername())) {
          return user;
      }
      return null;
  }
  ```



针对于上面四种测试场景，又分别对使用默认参数生成策略和基于 [MapStructPlus](https://mapstruct.plus) 两种情况分别测试。该条件可以参考[关联查询参数生成策略](/extension/to-param.html)。



在提供的测试用例中，尽可能排除其他方面的影响，这样子基本可以认为是引入框架后的性能损耗。



最终测试代码如下：

- 数据提供者 —— `UserDataProvider`

  ```java
  @Component
  public class UserDataProvider implements DataProvideService {
  
      private static String admin = "admin";
  
      private static String nickName = "管理员";
  
      private static User user;
  
      static {
          user = new User();
          user.setNickName(nickName);
      }
  
      @DataProvider("performanceGetNickNameByUsername")
      public String getNickNameByUsername(String username) {
          if (admin.equals(username)) {
              return nickName;
          }
          return null;
      }
  
      @DataProvider("performanceGetUserByUsername")
      public User getUserByUsername(String username) {
          if (admin.equals(username)) {
              return user;
          }
          return null;
      }
  
      @DataProvider("performanceGetNickname")
      public String getNickname(UserQueryReq userQueryReq) {
          if (admin.equals(userQueryReq.getUsername())) {
              return nickName;
          }
          return null;
      }
  
      @DataProvider("performanceGetUser")
      public User getUser(UserQueryReq userQueryReq) {
          if (admin.equals(userQueryReq.getUsername())) {
              return user;
          }
          return null;
      }
  
  }
  ```

- 测试类

  ```java
  @SpringBootTest
  public class PerformanceTest {
  
      private InjectRelation injectRelation;
  
      @Autowired
      private MapStructPlusMapToBeanHandle mapToBeanHandle;
  
      private Scene initScene() {
          final Scene scene = new Scene();
          scene.setUsername("admin");
          return scene;
      }
  
      private void recordExecute(InnerFunction innerFunction, String taskName) {
          StopWatch stopWatch = new StopWatch();
          for (int j = 0; j < 10; j++) {
              stopWatch.start(taskName + " : " + j);
              for (int i = 0; i < 1000000; i++) {
                  innerFunction.invoke();
              }
              stopWatch.stop();
          }
          System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
      }
  
      @Test
      public void performanceTest() {
          // 通过 json 转换
          injectRelation = new InjectRelation(new JsonMapToBeanHandle());
          sceneTest();
  
          // 通过 MapStructPlus 转换
          injectRelation = new InjectRelation(mapToBeanHandle);
          sceneTest();
      }
  
      public void sceneTest() {
          final Scene scene = initScene();
          // 场景一：参数和返回类型都一致，不需要转换
          recordExecute(() -> {
              injectRelation.injectRelation(scene, "sceneOneNickName");
          }, "scene one");
          // 场景二：参数类型相同，不需要转换，返回类型不同，需要获取其中的一个属性
          recordExecute(() -> {
              injectRelation.injectRelation(scene, "sceneTwoNickName");
          }, "scene two");
          // 场景三：参数类型不同，需要转换，返回类型相同
          recordExecute(() -> {
              injectRelation.injectRelation(scene, "sceneThreeNickName");
          }, "scene three");
          // 场景三：参数类型和返回类型都不相同
          recordExecute(() -> {
              injectRelation.injectRelation(scene, "sceneFourNickName");
          }, "scene four");
      }
  
  }
  ```





测试运行环境为：

- CPU：Intel i5 10400
- 内存：32 GB
- JDK：17


针对每个场景，执行10次一百万次的循环关联操作，最终结果如下：

- 使用默认的参数生成策略：

|场景一| 346 | 189  | 189  | 185  | 187  | 184  | 188  | 188  | 187  | 183  |
|----|---- | ---- | ---- |------| ---- | ---- | ---- | ---- | ---- | ---- |
|场景二| 530 | 424  | 423  | 421  | 424  | 422  | 421  | 421  | 430  | 432  |
|场景三| 2619 | 2271 | 2235 | 2235 | 2266 | 2248 | 2238 | 2235 | 2234 | 2233 |
|场景四| 2528 | 2515 | 2513 | 2519 | 2541 | 2515 | 2513 | 2513 | 2514 | 2510 |

- 使用 MapStructPlus 的参数生成策略：

| 场景一 | 209  | 186  | 186  | 186  | 186  | 186  | 188  | 189  | 187  | 188  |
| ------ | ---- | ---- | ---- |------| ---- | ---- | ---- | ---- | ---- | ---- |
| 场景二 | 405  | 407  | 404  | 406  | 407  | 406  | 405  | 405  | 407  | 405  |
| 场景三 | 418  | 389  | 389  | 385  | 386  | 384  | 383  | 384  | 385  | 384  |
| 场景四 | 621  | 620  | 620  | 621  | 618  | 621  | 618  | 619  | 620  | 619  |

