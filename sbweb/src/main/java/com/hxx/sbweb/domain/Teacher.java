package com.hxx.sbweb.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Teacher {
    private int id;
    private String name;
    private LocalDateTime createTime;
    private List<User> students;

}
