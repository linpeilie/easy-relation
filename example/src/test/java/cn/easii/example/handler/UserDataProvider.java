package cn.easii.example.handler;

import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.annotation.DataProvider;
import cn.easii.relation.core.DataProvideService;
import org.springframework.stereotype.Component;

@Component
public class UserDataProvider implements DataProvideService {

    private static String admin = "admin";

    private static String nickName = "管理员";

    private static User user;

    static {
        user = new User();
        user.setNickName(nickName);
    }

    @DataProvider("performanceGetNickNameByUsername")
    public String getNickNameByUsername(String username) {
        if (admin.equals(username)) {
            return nickName;
        }
        return null;
    }

    @DataProvider("performanceGetUserByUsername")
    public User getUserByUsername(String username) {
        if (admin.equals(username)) {
            return user;
        }
        return null;
    }

    @DataProvider("performanceGetNickname")
    public String getNickname(UserQueryReq userQueryReq) {
        if (admin.equals(userQueryReq.getUsername())) {
            return nickName;
        }
        return null;
    }

    @DataProvider("performanceGetUser")
    public User getUser(UserQueryReq userQueryReq) {
        if (admin.equals(userQueryReq.getUsername())) {
            return user;
        }
        return null;
    }

}
