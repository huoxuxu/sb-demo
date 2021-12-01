package com.hxx.tkMybatisTest.dal.mysql.testDB.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hxx.tkMybatisTest.dal.BaseSelectExample;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-12-01 13:52:43
 **/
@Data
public class T1Example extends BaseSelectExample {
    private List<Integer> ids;
    private Integer id;
    private String code;
    private String name;
    private Integer age;
    private Double score;
    private Integer enabled;
    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createtime;

}
