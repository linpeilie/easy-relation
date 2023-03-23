package cn.easii.relation.core.model;

import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import cn.easii.relation.core.RelationIdentifiers;
import java.util.List;
import lombok.Data;

@Data
public class User {

    private String username;

    private String nickName;

    @Relation(provider = "getRoleByUsername", condition = {@Condition(field = "username")})
    private Role role;

    @Relation(provider = "getPermissionsByUsername", condition = {@Condition(field = "username")})
    private List<Permission> permissions;

    private String createUsername;

    @Relation(provider = RelationIdentifiers.getUserByUsername, targetField = "nickName", condition = {
        @Condition(field = "createUsername", paramField = "username")})
    private String createNickName;

}
