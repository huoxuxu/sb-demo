package com.hxx.sbConsole.service.impl.test;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-09 12:39:43
 **/
public class StringTest {

    public void test() {
        System.out.println("==============test==============");
        String str = "1,2-3^4$5";
        // 原始的不能正常处理正则的分隔符，因为其入参需要传入正则表达式
        String[] split = str.split("^");
        System.out.println(split);

        // 可以正常处理！！！
        System.out.println(StringUtils.split(str, "^"));

        System.out.println("ok");
    }

    public void split() {
        System.out.println("==============test==============");
        String str = "1,2-3^4$5";
        // 原始的不能正常处理正则的分隔符，因为其入参需要传入正则表达式
        String[] split = str.split("^");
        System.out.println(split);

        // 可以正常处理！！！
        System.out.println(StringUtils.split(str, "^"));

        System.out.println("ok");

        {
            // output：1，2，3
            String str1="1 2 3  ";
            String[] s = str1.split(" ");
            System.out.println(s);
        }
    }
}
