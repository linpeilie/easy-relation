---
title: 自动关联数据
order: 4
category:
- 指南
description: EasyRelation easy-relation 指南 guide 自动关联数据 AutoRelation auto-relation
---

> since v1.1.1

当想要对一个方法的结果自动关联相关数据时，可以在方法上面增加 `@AutoRelation` 注解，
框架会对其方法返回的结果数据，自动关联配置的其他数据。

基于 SpringAOP 实现。

例如，在查询商品时，需要关联配置的其他店铺等信息：

- 之前

```java
public List<Goods> list(GoodsQuery goodsQuery) {
    List<Goods> list = goodsRepository.list(goodsQuery);
    injectRelation.injectRelation(list);
    return list;
}
```

- 使用自动关联数据注解

```java
@AutoRelation
public List<Goods> list(GoodsQuery goodsQuery) {
    return goodsRepository.list(goodsQuery);
}
```