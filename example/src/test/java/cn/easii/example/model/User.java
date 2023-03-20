package cn.easii.example.model;

import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import java.util.List;
import lombok.Data;

@Data
public class User {

    private String username;

    private String nickName;

    @Relation(handler = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

    @Relation(handler = "getPermissionsByUsername", condition = {@Condition(field = "username")})
    private List<Permission> permissions;

    private String createUsername;

    @Relation(handler = RelationIdentifiers.getUserByUsername, targetField = "nickName", condition = {
        @Condition(field = "createUsername", paramField = "username")}, useCache = true, cacheTimeout = 60)
    private String createNickName;

}
