package com.hxx.sbweb.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class User {
    private int id;
    @NotNull
    private String code;
    @NotNull
    private String name;
    private int age;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")// 出参解析
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")// 入参解析
    private LocalDateTime createTime;

    private String remark;
    private int theTeacher;

    @Valid
    @NotNull
    private Teacher teacher;
}
