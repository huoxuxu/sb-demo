package com.hxx.tkMybatisTest.dal.mysql.testDB.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-09 12:02:59
 **/
@Data
public class T1 {
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
