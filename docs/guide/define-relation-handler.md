---
title: 定义数据提供者
order: 2
category:
- 指南
description: EasyRelation 定义数据提供者 DataProvider 指南 Guide
---

## 定义数据提供者「DataProvider」

针对一个数据关联操作，DataProvider 是必须的，这是数据关联的数据提供方，在关联时，会自动调用该方法，获取并解析数据。

数据提供者所在的类要继承 `DataProviderService` 接口，同时在方法上面添加 `@DataProvider` 注解，同时在注解中指定唯一标识。

例如：

关联用户信息的数据提供者：


