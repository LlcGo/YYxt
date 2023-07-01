package com.lc;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @Author Lc
 * @Date 2023/7/1
 * @Description
 */
@Data
@Document("User")
public class User  {

    @Id
    private String id;

    private String username;

    private String password;


    private String nickName;
}
