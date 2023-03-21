package cn.easii.example.handler;

import cn.easii.example.Constants;
import cn.easii.example.RelationIdentifiers;
import cn.easii.example.model.Permission;
import cn.easii.example.model.PermissionQueryReq;
import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.RelationService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PermissionRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getPermissionsByUsername)
    public List<Permission> getPermissionsByUsername(PermissionQueryReq permissionQueryReq) {
        System.out.println("permissionQueryReq:" + permissionQueryReq);
        if (!Constants.ENABLED.equals(permissionQueryReq.getState())) {
            return Collections.emptyList();
        }
        if ("admin".equals(permissionQueryReq.getUsername())) {
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
