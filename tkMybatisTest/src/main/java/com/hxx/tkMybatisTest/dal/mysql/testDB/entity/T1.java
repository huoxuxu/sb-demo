package com.hxx.tkMybatisTest.dal.mysql.testDB.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:02:59
 **/
@Data
public class T1 {
    private Integer id;
    private String code;
    private String name;
    private Double score;
    private Boolean enabled;
    private LocalDateTime birthday;
    private LocalDateTime createTime;

}
