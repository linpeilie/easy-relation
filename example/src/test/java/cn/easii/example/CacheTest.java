package cn.easii.example;

import cn.easii.example.model.User;
import cn.easii.relation.core.InjectRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheTest {

    @Autowired
    private InjectRelation injectRelation;

    @Test
    public void testCache() {
        final User user = new User();
        user.setCreateUsername("admin");

        for (int i = 0; i < 10; i++) {
            injectRelation.injectRelation(user, "createNickName");
        }
    }

}
