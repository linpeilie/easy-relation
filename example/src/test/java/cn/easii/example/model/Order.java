package cn.easii.example.model;

import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import lombok.Data;

@Data
public class Order {

    private String source;

    private String orderId;

    private String username;

    @Relation(provider = RelationIdentifiers.getUserByUsername, targetField = "nickName",
        condition = {
            @Condition(field = "username"),
            @Condition(field = "source")
        })
    private String nickName;

}
