package com.hxx.sbConsole.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:12:29
 **/
@Data
public class User {
    private Integer id;
    private String code;
    private String name;
    private BigDecimal scope;
    private Boolean isMale;
}
