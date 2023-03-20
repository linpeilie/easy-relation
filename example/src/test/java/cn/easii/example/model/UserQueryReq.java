package cn.easii.example.model;

import io.github.linpeilie.annotations.AutoMapMapper;
import lombok.Data;

@Data
@AutoMapMapper
public class UserQueryReq {

    private String username;

    private Long userId;

    private Boolean isDeleted;

}
