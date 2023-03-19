package cn.easii.relation.core.utils;

import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.lang.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectUtilsTest {

    private UserQueryReq userQueryReq = null;

    @BeforeEach
    public void before() {
        userQueryReq = new UserQueryReq();
        userQueryReq.setUsername("Tom");
    }

    @Test
    void getGetter() throws InvocationTargetException, IllegalAccessException {
        final Method usernameGetter = ReflectUtils.getGetter(userQueryReq.getClass(), "username");
        Assert.notNull(usernameGetter);
        final Object username = usernameGetter.invoke(userQueryReq);
        Assert.equals(username, "Tom");
    }

    @Test
    void getSetter() throws InvocationTargetException, IllegalAccessException {
        final Method userIdSetter = ReflectUtils.getSetter(userQueryReq.getClass(), "userId");
        Assert.notNull(userIdSetter);
        userIdSetter.invoke(userQueryReq, 100001L);
        Assert.equals(userQueryReq.getUserId(), 100001L);
    }
}