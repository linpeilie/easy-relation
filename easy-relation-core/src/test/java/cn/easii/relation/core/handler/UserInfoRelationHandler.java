package cn.easii.relation.core.handler;

import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.RelationIdentifiers;
import cn.easii.relation.core.RelationService;
import cn.easii.relation.core.model.User;
import cn.easii.relation.core.model.UserQueryReq;

public class UserInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        if ("admin".equals(req.getUsername())) {
            final User user = new User();
            user.setUsername("admin");
            user.setNickName("管理员");
            return user;
        }
        return null;
    }

}
