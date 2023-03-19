package cn.easii.relation.core.utils;

import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.lang.Assert;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CollectionUtilsTest {

    @Test
    public void mapTest() {
        List<UserQueryReq> userQueryReqList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserQueryReq userQueryReq = new UserQueryReq();
            userQueryReq.setUsername("username" + i);
            userQueryReqList.add(userQueryReq);
        }

        final List<String> usernameList = CollectionUtils.map(userQueryReqList, UserQueryReq::getUsername);
        Assert.isTrue(usernameList.size() == 10);
        for (int i = 0; i < 10; i++) {
            final String username = usernameList.get(i);
            Assert.equals(username, "username" + i);
        }
    }

    @Test
    public void nullMapTest() {
        List<UserQueryReq> list = null;
        final List<String> usernameList = CollectionUtils.map(list, UserQueryReq::getUsername);
        Assert.isTrue(usernameList != null && usernameList.isEmpty());
    }

}
