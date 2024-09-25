package com.hxx.sb.dal.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserInfo {
    /**
     * id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 存款
     */
    private BigDecimal deposit;
    /**
     * 生日
     */
    private LocalDateTime birth;
}
