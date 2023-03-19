package cn.easii.relation.core.utils;

import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectUtilsTest {

    @Test
    void testToString() {
        String str = null;
        final String toString = ObjectUtils.toString(str);
        Assert.equals(toString, "");
    }
}