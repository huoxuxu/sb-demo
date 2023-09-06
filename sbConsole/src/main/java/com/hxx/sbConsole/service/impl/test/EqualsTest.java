package com.hxx.sbConsole.service.impl.test;

import java.util.Objects;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-09 12:40:46
 **/
public class EqualsTest {
    public static void main(String[] args) {
        try {
            demo();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }

    public static void demo() {
        int a = 1024089;
        Integer b = Integer.valueOf("1024089");
        Integer b1 = Integer.valueOf("1024089");
        System.out.println(a == b);// true
        System.out.println(b1 == b);// false
        System.out.println(Objects.equals(a, b)); // true
        System.out.println(Objects.equals(b1, b)); // true
    }
}
