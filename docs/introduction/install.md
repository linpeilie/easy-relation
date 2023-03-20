---
title: 安装
order: 2
category:
- 介绍 
tag:
- 安装
description: EasyRelation 安装 依赖 install
---

## 非 SpringBoot 环境

### Maven

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

### Gradle

```groovy
dependencies {
    implementation group: 'cn.easii', name: 'easy-relation-core', version: '最新版本'
}
```

## SpringBoot 环境

### Maven

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

### Gradle

```groovy
dependencies {
    implementation group: 'cn.easii', name: 'easy-relation-spring-boot-starter', version: '最新版本'
}
```
