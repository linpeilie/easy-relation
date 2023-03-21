package cn.easii.relation.core;

import cn.easii.relation.MapToBeanHandle;
import cn.easii.relation.core.bean.DynamicConditionMeta;
import cn.easii.relation.core.bean.RelationHandlerMeta;
import cn.easii.relation.core.bean.RelationMeta;
import cn.easii.relation.core.handler.PermissionRelationHandler;
import cn.easii.relation.core.handler.RoleInfoRelationHandler;
import cn.easii.relation.core.handler.UserInfoRelationHandler;
import cn.easii.relation.core.model.User;
import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.date.StopWatch;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

class InjectRelationTest {

    private InjectRelation injectRelation;

    private PermissionRelationHandler permissionRelationHandler;

    private RoleInfoRelationHandler roleInfoRelationHandler;

    private UserInfoRelationHandler userInfoRelationHandler;

    @BeforeEach
    public void before() {
        injectRelation = new InjectRelation();
        userInfoRelationHandler = new UserInfoRelationHandler();
        roleInfoRelationHandler = new RoleInfoRelationHandler();
        permissionRelationHandler = new PermissionRelationHandler();
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getUserByUsername) == null) {
            RelationHandlerRepository.registerHandler(userInfoRelationHandler);
        }
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getRoleByUsername) == null) {
            RelationHandlerRepository.registerHandler(roleInfoRelationHandler);
        }
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getPermissionsByUsername) == null) {
            RelationHandlerRepository.registerHandler(permissionRelationHandler);
        }
    }

    private User initUser() {
        User user = new User();
        user.setUsername("admin");
        user.setNickName("管理员");
        user.setCreateUsername("admin");
        return user;
    }

    @Test
    void injectRelation() {
        for (int i = 0; i < 100; i++) {
            injectRelationSet();
        }
        final User user = initUser();
        injectRelation.injectRelation(user);
        System.out.println(user);
    }

    @Test
    void testInjectRelation() throws Exception {
        for (int i = 0; i < 1000; i++) {
            directSet();
            injectRelationSet();
        }
        StopWatch stopWatch = new StopWatch("性能比较");
        stopWatch.start("直接调用方法取值set");
        for (int i = 0; i < 1000000; i++) {
            directSet();
        }
        stopWatch.stop();
        stopWatch.start("自动注入关联数据");
        for (int i = 0; i < 1000000; i++) {
            injectRelationSet();
        }
        stopWatch.stop();
        final RelationHandlerMeta handler = RelationHandlerRepository.getHandler(RelationIdentifiers.getRoleByUsername);
        stopWatch.start("invoke handler");
        for (int i = 0; i < 1000000; i++) {
            handler.getHandlerFunction().apply("admin");
        }
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    private void directSet() {
        final User user = initUser();
        user.setRole(roleInfoRelationHandler.getRoleByUsername(user.getUsername()));
        user.setPermissions(permissionRelationHandler.getPermissionsByUsername(user.getUsername()));
        UserQueryReq userQueryReq = new UserQueryReq();
        userQueryReq.setUsername(user.getCreateUsername());
        user.setCreateNickName(userInfoRelationHandler.getUserByUsername(userQueryReq).getNickName());
    }

    private void injectRelationSet() {
        final User user = initUser();
        injectRelation.injectRelation(user);
    }

}