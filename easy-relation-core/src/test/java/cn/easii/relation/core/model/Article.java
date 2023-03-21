package cn.easii.relation.core.model;

import cn.easii.relation.annotation.Condition;
import cn.easii.relation.annotation.Relation;
import cn.easii.relation.core.RelationIdentifiers;
import lombok.Data;

@Data
public class Article {

    private String content;

    private String authorUsername;

    @Relation(handler = RelationIdentifiers.getUserByUsername, condition = {
        @Condition(field = "authorUsername", paramField = "username")}, targetField = "nickName",
        useCache = true, cacheTimeout = 5)
    private String nickName;

}
