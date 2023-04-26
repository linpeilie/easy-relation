---
title: MapStructPlus
order: 1
category:
- 插件
description: EasyRelation easy-relation 插件 Plugins plugins mapstruct-plus MapStructPlus
---

如果项目中使用 [MapStructPlus](https://mapstruct.plus)，强烈建议将其与 EasyRelation 整合使用，
[MapStructPlus](https://mapstruct.plus) 提供了更加高效的 `Map<String, Object>` 转对象的功能「详情可以参考[Map 转对象](https://mapstruct.plus/guide/map-to-class.html)文档」关于性能方面的提升，可以查看[性能对比](/introduction/performance.html)。

使用比较简单，添加 `easy-relation-mapstruct-plus` 依赖包即可。

```xml
<dependency>
    <groupId>cn.easii</groupId>
    <artifactId>easy-relation-mapstruct-plus</artifactId>
    <version>最新版本</version>
</dependency>
```

