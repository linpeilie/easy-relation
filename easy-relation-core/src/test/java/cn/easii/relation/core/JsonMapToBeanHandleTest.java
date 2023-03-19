package cn.easii.relation.core;

import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.lang.Assert;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JsonMapToBeanHandleTest {

    @Test
    public void mapToBeanTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "easii");
        map.put("userId", 10001);
        map.put("isDeleted", false);

        final UserQueryReq userQueryReq = new JsonMapToBeanHandle().mapToBean(map, UserQueryReq.class);
        System.out.println(userQueryReq);
        Assert.equals(userQueryReq.getUsername(), "easii");
        Assert.equals(userQueryReq.getUserId(), 10001L);
        Assert.isFalse(userQueryReq.getIsDeleted());
    }

}
