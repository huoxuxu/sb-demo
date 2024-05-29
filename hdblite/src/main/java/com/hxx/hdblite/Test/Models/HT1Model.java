package com.hxx.hdblite.Test.Models;

import java.util.Date;

public class HT1Model {
    private int Id;
    private String Name;
    private String Name2;
    private double Score;
    private int Age;
    private String Remark;
    private Date CreateDate;
    private Date CreateTime;
    private Date UpdateDate;

    //get set================
    public int getId() {
        return Id;
    }

    public void setId(int ID) {
        this.Id = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
    }

    public Date getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(Date updateDate) {
        UpdateDate = updateDate;
    }
}
