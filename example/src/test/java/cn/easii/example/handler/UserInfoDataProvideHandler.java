package cn.easii.example.handler;

import cn.easii.example.RelationIdentifiers;
import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.DataProvideService;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

@Component
public class UserInfoDataProvideHandler implements DataProvideService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        System.out.println("req:" + req);
        if (StrUtil.isEmpty(req.getUsername())) {
            throw new IllegalArgumentException("username is empty");
        }
        if ("admin".equals(req.getUsername())) {
            final User user = new User();
            user.setUsername("admin");
            user.setNickName("管理员");
            return user;
        }
        return null;
    }

}
