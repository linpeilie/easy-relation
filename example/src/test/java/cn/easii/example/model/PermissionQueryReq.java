package cn.easii.example.model;

import io.github.linpeilie.annotations.AutoMapMapper;
import lombok.Data;

@Data
@AutoMapMapper
public class PermissionQueryReq {

    private String username;

    private Integer state;

}
