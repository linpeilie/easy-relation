package cn.easii.example;

import cn.easii.example.handler.PermissionDataProviderHandler;
import cn.easii.example.handler.RoleInfoDataProviderHandler;
import cn.easii.example.handler.UserInfoDataProviderHandler;
import cn.easii.example.model.Order;
import cn.easii.example.model.PermissionQueryReq;
import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.core.DataProviderRepository;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.bean.DataProviderMeta;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InjectRelationTest {

    @Autowired
    private InjectRelation injectRelation;

    @Autowired
    private PermissionDataProviderHandler permissionRelationHandler;

    @Autowired
    private RoleInfoDataProviderHandler roleInfoRelationHandler;

    @Autowired
    private UserInfoDataProviderHandler userInfoRelationHandler;

    private User initUser() {
        User user = new User();
        user.setUsername("admin");
        user.setNickName("管理员");
        user.setCreateUsername("admin");
        return user;
    }

    @Test
    void quickStart() {
        Order order = new Order();
        order.setOrderId("2f453910375641648ab3a2fc6e3328ef");
        order.setUsername("admin");
        injectRelation.injectRelation(order);
        System.out.println(order);  // Order(orderId=2f453910375641648ab3a2fc6e3328ef, username=admin, nickName=管理员)
        Assert.equals(order.getNickName(), "管理员");
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
        PermissionQueryReq permissionQueryReq = new PermissionQueryReq();
        permissionQueryReq.setState(Constants.ENABLED);
        permissionQueryReq.setUsername("admin");
        user.setPermissions(permissionRelationHandler.getPermissionsByUsername(permissionQueryReq));
        UserQueryReq userQueryReq = new UserQueryReq();
        userQueryReq.setUsername(user.getCreateUsername());
        user.setCreateNickName(userInfoRelationHandler.getUserByUsername(userQueryReq).getNickName());
    }

    private void injectRelationSet() {
        final User user = initUser();
        injectRelation.injectRelation(user);
    }

}