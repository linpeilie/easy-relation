package cn.easii.relation.core.handler;

import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.RelationIdentifiers;
import cn.easii.relation.core.RelationService;
import cn.easii.relation.core.model.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermissionRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getPermissionsByUsername)
    public List<Permission> getPermissionsByUsername(String username) {
        if ("admin".equals(username)) {
            List<Permission> permissions = new ArrayList<>();
            final Permission permission1 = new Permission();
            permission1.setResource("/user");
            final Permission permission2 = new Permission();
            permission2.setResource("/role");
            final Permission permission3 = new Permission();
            permission3.setResource("/permission");
            permissions.add(permission1);
            permissions.add(permission2);
            permissions.add(permission3);
            return permissions;
        }
        return Collections.emptyList();
    }

}
