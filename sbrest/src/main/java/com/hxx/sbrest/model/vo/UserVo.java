package com.hxx.sbrest.model.vo;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 11:19:45
 **/

public class UserVo {
    private String id;
    @NotEmpty(message = "用户名不能为空")
    private String username;
//    @Size(min=6 ,max= 20 ,message = "密码最少6位，最高20位")
//    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
