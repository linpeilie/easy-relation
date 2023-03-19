package cn.easii.relation.core;

import cn.easii.relation.RelationCache;
import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRelationCacheTest {

    private RelationCache relationCache = new DefaultRelationCache();

    private UserQueryReq userQueryReq;

    @BeforeEach
    public void before() {
        userQueryReq = new UserQueryReq();
        userQueryReq.setUsername("Marry");
    }

    @Test
    void setAndGet() throws InterruptedException {
        relationCache.set("user", userQueryReq, 3000);
        UserQueryReq user = (UserQueryReq) relationCache.get("user");
        Assert.notNull(user);
        Assert.equals(user.getUsername(), "Marry");
        Thread.sleep(3000);
        final Object userObj = relationCache.get("user");
        Assert.isNull(userObj);
    }

}