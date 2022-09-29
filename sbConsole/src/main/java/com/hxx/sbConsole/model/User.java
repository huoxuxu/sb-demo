package com.hxx.sbConsole.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime birthday;
}
