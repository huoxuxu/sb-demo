package com.hxx.sbConsole.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class Employee {
    public Employee(int id, String jake, Gender male,
                    LocalDate of, double v, boolean b, Date toDate) {
        id = id;
        name = jake;
        gender = male;
        createTime = of;
        income = v;
        alive = b;
        birthday = toDate;
    }

    public static enum Gender {
        MALE, FEMALE
    }

    public Employee() {

    }

    private long id;
    private String name;
    private Gender gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate createTime;
    private int age;
    private double income;
    private boolean alive;
    private Date birthday;
    private BigDecimal salary;

    public boolean isMale() {
        return this.gender == Gender.MALE;
    }

    public boolean isFemale() {
        return this.gender == Gender.FEMALE;
    }

}
