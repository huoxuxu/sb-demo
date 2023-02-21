package com.hxx.sbservice.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 13:56:42
 **/
@Data
public class JdbcModel {
    private String driver;
    private String url;
    private String userName;
    private String password;

}
