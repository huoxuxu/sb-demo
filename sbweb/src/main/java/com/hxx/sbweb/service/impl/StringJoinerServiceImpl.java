package com.hxx.sbweb.service.impl;

import org.springframework.stereotype.Service;

import java.util.StringJoiner;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-26 17:25:10
 **/
@Service
public class StringJoinerServiceImpl {

    public void Test1(){
        StringJoiner joiner=new StringJoiner(",");
        joiner.add("Hello");
        joiner.add("我的");
        String str=joiner.toString();
        System.out.println(str);
        //Hello,我的
    }

    public void Test2(){
        StringJoiner joiner=new StringJoiner(",","[","]");
        joiner.setEmptyValue("Hello");
        String str=joiner.toString();
        System.out.println(str);
        //Hello
    }

    public void Test3(){
        StringJoiner joiner=new StringJoiner(",","[","]");
        joiner.setEmptyValue("Hello");
        String str= String.join(",","Hello","我的");
        System.out.println(str);
        //Hello
    }


}
