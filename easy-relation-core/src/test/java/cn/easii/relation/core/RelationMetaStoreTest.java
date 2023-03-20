package cn.easii.relation.core;

import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.core.model.Role;
import cn.easii.relation.core.model.User;
import cn.hutool.core.lang.Assert;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RelationMetaStoreTest {

    @Test
    void getRelationMetaList() throws InvocationTargetException, IllegalAccessException {
        final List<RelationMeta> relationMetaList = RelationMetaStore.getRelationMetaList(User.class);
        System.out.println(relationMetaList);
        Assert.isTrue(relationMetaList.size() == 3);
        for (RelationMeta relationMeta : relationMetaList) {
            Assert.notNull(relationMeta.getFieldSetter());
            Assert.notBlank(relationMeta.getHandlerIdentifier());
            Assert.notEmpty(relationMeta.getConditions());
            if ("role".equals(relationMeta.getField())) {
                final User user = new User();
                final Role role = new Role();
                role.setRoleName("Admin");
                final Method fieldSetter = relationMeta.getFieldSetter();
                fieldSetter.invoke(user, role);
                Assert.notNull(user.getRole());
                Assert.equals("Admin", user.getRole().getRoleName());
            } else if ("permissions".equals(relationMeta.getField())) {

            } else if ("createUserName".equals(relationMeta.getField())) {

            } else {
                Assert.isTrue(true);
            }
        }
    }
}