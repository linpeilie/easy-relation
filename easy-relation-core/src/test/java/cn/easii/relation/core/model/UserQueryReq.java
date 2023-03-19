package cn.easii.relation.core.model;

import lombok.Data;

@Data
public class UserQueryReq {

    private String username;

    private Long userId;

    private Boolean isDeleted;

}
