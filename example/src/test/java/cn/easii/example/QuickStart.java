package cn.easii.example;

import cn.easii.example.handler.UserInfoRelationHandler;
import cn.easii.example.model.Order;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.RelationHandlerRepository;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuickStart {

    private InjectRelation injectRelation;

    @BeforeEach
    public void before() {
        // 注册用户信息获取接口
        RelationHandlerRepository.registerHandler(new UserInfoRelationHandler());
        injectRelation = new InjectRelation();
    }

    @Test
    public void quickStart() {
        // 获取 order 信息
        final Order order = getOrder("eb35e18caa552284b39d427c1e06f9f7");
        injectRelation.injectRelation(order);
        System.out.println(order);  // Order(orderId=eb35e18caa552284b39d427c1e06f9f7, username=admin, nickName=管理员)
        Assert.equals(order.getNickName(), "管理员");
    }

    private Order getOrder(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setUsername("admin");
        return order;
    }

}
