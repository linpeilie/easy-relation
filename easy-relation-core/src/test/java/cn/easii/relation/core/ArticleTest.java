package cn.easii.relation.core;

import cn.easii.relation.core.handler.PermissionDataProvideHandler;
import cn.easii.relation.core.handler.RoleInfoDataProvideHandler;
import cn.easii.relation.core.handler.UserInfoDataProvideHandler;
import cn.easii.relation.core.model.Article;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArticleTest {

    private InjectRelation injectRelation;

    private PermissionDataProvideHandler permissionRelationHandler;

    private RoleInfoDataProvideHandler roleInfoRelationHandler;

    private UserInfoDataProvideHandler userInfoRelationHandler;

    @BeforeEach
    public void before() {
        injectRelation = new InjectRelation();
        userInfoRelationHandler = new UserInfoDataProvideHandler();
        roleInfoRelationHandler = new RoleInfoDataProvideHandler();
        permissionRelationHandler = new PermissionDataProvideHandler();
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getUserByUsername) == null) {
            DataProviderRepository.registerProvider(userInfoRelationHandler);
        }
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getRoleByUsername) == null) {
            DataProviderRepository.registerProvider(roleInfoRelationHandler);
        }
        if (DataProviderRepository.getDataProvider(RelationIdentifiers.getPermissionsByUsername) == null) {
            DataProviderRepository.registerProvider(permissionRelationHandler);
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
//        injectRelation.injectRelation(article);
    }

    public Article getArticle(String username) {
        final Article article = new Article();
        article.setContent("EasyRelation是一个简单、快速、强大的数据自动关联框架");
        article.setAuthorUsername(username);
        return article;
    }

}
