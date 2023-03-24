package cn.easii.example.model;

import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import java.io.Serializable;
import lombok.Data;

@Data
public class Scene implements Serializable {

    private String username;

    @Relation(provider = "performanceGetNickNameByUsername", condition = {
        @Condition(field = "username")
    })
    private String sceneOneNickName;

    @Relation(provider = "performanceGetUserByUsername", condition = {
        @Condition(field = "username")
    }, targetField = "nickName")
    private String sceneTwoNickName;

    @Relation(provider = "performanceGetNickname", condition = {
        @Condition(field = "username")
    })
    private String sceneThreeNickName;

    @Relation(provider = "performanceGetUser", condition = {
        @Condition(field = "username")
    }, targetField = "nickName")
    private String sceneFourNickName;

}
