package cn.easii.example;

import cn.easii.example.handler.PermissionRelationHandler;
import cn.easii.example.handler.RoleInfoRelationHandler;
import cn.easii.example.handler.UserInfoRelationHandler;
import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.RelationHandlerRepository;
import cn.easii.relation.core.bean.RelationHandlerMeta;
import cn.hutool.core.date.StopWatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    @Autowired
    private PermissionRelationHandler permissionRelationHandler;

    @Autowired
    private RoleInfoRelationHandler roleInfoRelationHandler;

    @Autowired
    private UserInfoRelationHandler userInfoRelationHandler;

    private User initUser() {
        User user = new User();
        user.setUsername("admin");
        user.setNickName("管理员");
        user.setCreateUsername("admin");
        return user;
    }

    @Test
    void injectRelation() {
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