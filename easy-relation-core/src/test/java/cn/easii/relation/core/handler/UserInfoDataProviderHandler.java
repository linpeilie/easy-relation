package cn.easii.relation.core.handler;

import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.RelationIdentifiers;
import cn.easii.relation.core.DataProviderService;
import cn.easii.relation.core.model.User;
import cn.easii.relation.core.model.UserQueryReq;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

public class UserInfoDataProviderHandler implements DataProviderService {

    @DataProvider(RelationIdentifiers.getUserByUsername)
    public User getUserByUsername(UserQueryReq req) {
        System.out.println(DateUtil.now() + "req : " + req);
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
