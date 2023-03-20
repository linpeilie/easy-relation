package cn.easii.example.model;

import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import lombok.Data;

@Data
public class Order {

    private String orderId;

    private String username;

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName",
        condition = {@Condition(field = "username")})
    private String nickName;

}
