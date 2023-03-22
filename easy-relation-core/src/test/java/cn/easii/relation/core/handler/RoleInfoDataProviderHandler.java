package cn.easii.relation.core.handler;

import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.RelationIdentifiers;
import cn.easii.relation.core.DataProviderService;
import cn.easii.relation.core.model.Role;

public class RoleInfoDataProviderHandler implements DataProviderService {

    @DataProvider(RelationIdentifiers.getRoleByUsername)
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
