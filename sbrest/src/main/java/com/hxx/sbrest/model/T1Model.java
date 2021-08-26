package com.hxx.sbrest.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-06 15:35:11
 **/
@Data
public class T1Model {
    private String code;
    private String Name;
    private Integer age;
    private Double score;
    
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;
}
