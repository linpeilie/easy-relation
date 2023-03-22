package cn.easii.relation.core;

import cn.easii.relation.core.bean.DataProviderMeta;
import cn.easii.relation.core.handler.PermissionDataProviderHandler;
import cn.easii.relation.core.handler.RoleInfoDataProviderHandler;
import cn.easii.relation.core.handler.UserInfoDataProviderHandler;
import cn.easii.relation.core.model.User;
import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.date.StopWatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InjectRelationTest {

    private InjectRelation injectRelation;

    private PermissionDataProviderHandler permissionRelationHandler;

    private RoleInfoDataProviderHandler roleInfoRelationHandler;

    private UserInfoDataProviderHandler userInfoRelationHandler;

    @BeforeEach
    public void before() {
        injectRelation = new InjectRelation();
        userInfoRelationHandler = new UserInfoDataProviderHandler();
        roleInfoRelationHandler = new RoleInfoDataProviderHandler();
        permissionRelationHandler = new PermissionDataProviderHandler();
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getUserByUsername) == null) {
            DataProviderRepository.registerProvider(userInfoRelationHandler);
        }
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getRoleByUsername) == null) {
            DataProviderRepository.registerProvider(roleInfoRelationHandler);
        }
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getPermissionsByUsername) == null) {
            DataProviderRepository.registerProvider(permissionRelationHandler);
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
        final DataProviderMeta handler = DataProviderRepository.getDataProvider(RelationIdentifiers.getRoleByUsername);
        stopWatch.start("invoke handler");
        for (int i = 0; i < 1000000; i++) {
            handler.getFunction().apply("admin");
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