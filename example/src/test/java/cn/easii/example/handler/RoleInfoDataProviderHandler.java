package cn.easii.example.handler;

import cn.easii.example.RelationIdentifiers;
import cn.easii.example.model.Role;
import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.DataProviderService;
import org.springframework.stereotype.Component;

@Component
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
