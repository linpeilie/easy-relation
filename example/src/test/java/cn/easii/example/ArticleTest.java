package cn.easii.example;

import cn.easii.example.handler.UserInfoDataProvideHandler;
import cn.easii.example.model.Article;
import cn.easii.relation.core.DataProviderRepository;
import cn.easii.relation.core.InjectRelation;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleTest {

    @Autowired
    private InjectRelation injectRelation;

    @Autowired
    private UserInfoDataProvideHandler userInfoRelationHandler;

    @BeforeEach
    public void before() {
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getUserByUsername) == null) {
            DataProviderRepository.registerProvider(userInfoRelationHandler);
        }
    }

    @Test
    void testCache() throws InterruptedException {
        Article article = getArticle("admin");
        injectRelation.injectRelation(article);
        System.out.println(article);
        Assert.equals(article.getNickName(), "管理员");
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
            article.setNickName(null);
            injectRelation.injectRelation(article);
        }
        Assert.equals(article.getNickName(), "管理员");
    }


    @Test
    void testThrowException() {
        final Article article = getArticle(null);
        injectRelation.injectRelation(article);
    }

    public Article getArticle(String username) {
        final Article article = new Article();
        article.setContent("EasyRelation是一个简单、快速、强大的数据自动关联框架");
        article.setAuthorUsername(username);
        return article;
    }

}
