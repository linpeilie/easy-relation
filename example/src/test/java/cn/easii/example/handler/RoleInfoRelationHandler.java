package cn.easii.example.handler;

import cn.easii.example.RelationIdentifiers;
import cn.easii.example.model.Role;
import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.RelationService;
import org.springframework.stereotype.Component;

@Component
public class RoleInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getRoleByUsername)
    public Role getRoleByUsername(String username) {
        if ("admin".equals(username)) {
            final Role role = new Role();
            role.setRoleId("100001");
            role.setRoleName("系统管理员");
            return role;
        }
        return null;
    }

}
