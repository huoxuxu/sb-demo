package com.hxx.sbweb.domain;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private int age;
    private LocalDateTime createTime;
    private String remark;
    private int theTeacher;

    private Teacher teacher;

    //
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getTheTeacher() {
        return theTeacher;
    }

    public void setTheTeacher(int theTeacher) {
        this.theTeacher = theTeacher;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }


}
