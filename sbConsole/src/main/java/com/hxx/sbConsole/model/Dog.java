package com.hxx.sbConsole.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 15:57:32
 **/
public class Dog {
    private String name;
    private String age;

    // 默认 protected
    protected int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    // 默认 protected
    String demoMethod(){
        return "demoMethod";
    }
}
