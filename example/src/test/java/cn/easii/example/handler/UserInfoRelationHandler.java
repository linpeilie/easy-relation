package cn.easii.example.handler;

import cn.easii.example.RelationIdentifiers;
import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.annotation.RelationHandler;
import cn.easii.relation.core.RelationService;
import org.springframework.stereotype.Component;

@Component
public class UserInfoRelationHandler implements RelationService {

    @RelationHandler(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        System.out.println("req = " + req);
        if ("admin".equals(req.getUsername())) {
            final User user = new User();
            user.setUsername("admin");
            user.setNickName("管理员");
            return user;
        }
        return null;
    }

}
