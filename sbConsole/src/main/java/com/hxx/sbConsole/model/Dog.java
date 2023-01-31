package com.hxx.sbConsole.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 15:57:32
 **/
@lombok.Data
public class Dog {
    private String name;
    private String age;

    // 默认 protected
    protected int score;

    // 默认 protected
    String demoMethod(){
        return "demoMethod";
    }
}
