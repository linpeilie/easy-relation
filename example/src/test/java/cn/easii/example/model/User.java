package cn.easii.example.model;

import cn.easii.example.Constants;
import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.CacheStrategy;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.ConstantsCondition;
import cn.easii.relation.annotation.Relation;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class User implements Serializable {

    private String username;

    private String nickName;

    private String icon;

    @Relation(provider = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

    @Relation(provider = RelationIdentifiers.getPermissionsByUsername, condition = {
        @Condition(field = "username")}, constantsCondition = {@ConstantsCondition(field = "state", value = "1")})
    private List<Permission> permissions;

    private String createUsername;

    @Relation(provider = RelationIdentifiers.getUserByUsername, targetField = "nickName", condition = {
        @Condition(field = "createUsername", paramField = "username")}, cacheStrategy = CacheStrategy.ENABLE)
    private String createNickName;

}
