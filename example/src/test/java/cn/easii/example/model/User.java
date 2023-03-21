package cn.easii.example.model;

import cn.easii.example.Constants;
import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.ConstantsCondition;
import cn.easii.relation.annotation.Relation;
import java.util.List;
import lombok.Data;

@Data
public class User {

    private String username;

    private String nickName;

    private String icon;

    @Relation(handler = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

    @Relation(handler = RelationIdentifiers.getPermissionsByUsername, condition = {
        @Condition(field = "username")}, constantsCondition = {@ConstantsCondition(field = "state", value = "1")})
    private List<Permission> permissions;

    private String createUsername;

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName", condition = {
        @Condition(field = "createUsername", paramField = "username")}, useCache = true, cacheTimeout = 60)
    private String createNickName;

}
