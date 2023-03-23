package cn.easii.example.model;

import cn.easii.example.RelationIdentifiers;
import cn.easii.relation.CacheStrategy;
import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import lombok.Data;

@Data
public class Article {

    private String content;

    private String authorUsername;

    @Relation(provider = RelationIdentifiers.getUserByUsername, condition = {
        @Condition(field = "authorUsername", paramField = "username")}, targetField = "nickName",
        cacheStrategy = CacheStrategy.ENABLE)
    private String nickName;

}
