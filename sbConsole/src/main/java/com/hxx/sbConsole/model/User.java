package com.hxx.sbConsole.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:12:29
 **/
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 9005093432700355764L;

    private Integer id;
    private String code;
    private String name;
    private BigDecimal scope;
    private Boolean male;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime birthday;

    private List<Integer> ls;

}
