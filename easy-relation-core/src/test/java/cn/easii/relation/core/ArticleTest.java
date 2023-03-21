package cn.easii.relation.core;

import cn.easii.relation.core.handler.PermissionRelationHandler;
import cn.easii.relation.core.handler.RoleInfoRelationHandler;
import cn.easii.relation.core.handler.UserInfoRelationHandler;
import cn.easii.relation.core.model.Article;
import cn.hutool.core.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArticleTest {

    private InjectRelation injectRelation;

    private PermissionRelationHandler permissionRelationHandler;

    private RoleInfoRelationHandler roleInfoRelationHandler;

    private UserInfoRelationHandler userInfoRelationHandler;

    @BeforeEach
    public void before() {
        injectRelation = new InjectRelation();
        userInfoRelationHandler = new UserInfoRelationHandler();
        roleInfoRelationHandler = new RoleInfoRelationHandler();
        permissionRelationHandler = new PermissionRelationHandler();
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getUserByUsername) == null) {
            RelationHandlerRepository.registerHandler(userInfoRelationHandler);
        }
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getRoleByUsername) == null) {
            RelationHandlerRepository.registerHandler(roleInfoRelationHandler);
        }
        if (RelationHandlerRepository.getHandler(RelationIdentifiers.getPermissionsByUsername) == null) {
            RelationHandlerRepository.registerHandler(permissionRelationHandler);
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
