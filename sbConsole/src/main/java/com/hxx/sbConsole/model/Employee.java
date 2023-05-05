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
    private long id;
    private String name;
    private Gender gender;
    private int age;
    private double income;
    private boolean alive;
    private Date birthday;
    private BigDecimal salary;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDate createTime;

    public Employee(int id, String name, Gender male, int age, double income, boolean alive, Date birthday, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.gender = male;
        this.age = age;
        this.income = income;
        this.alive = alive;
        this.birthday = birthday;
        this.salary = salary;
        this.createTime = LocalDate.now();
    }

    public static enum Gender {
        MALE, FEMALE
    }

    public Employee() {

    }

    public boolean isMale() {
        return this.gender == Gender.MALE;
    }

    public boolean isFemale() {
        return this.gender == Gender.FEMALE;
    }

    public String getUK() {
        return gender + "-" + age;
    }
}
