package com.hxx.sbConsole.service.impl.demo.basic;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-09 10:46:54
 **/
public class CollectionDemo {
    public static void main(String[] args) {
        try {
            case0();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
    }

    static void case0() {
        // HashSet 支持add null
        Set<Integer> hashSet = new HashSet<>();
        hashSet.add(0);
        hashSet.add(1024);
        hashSet.add(2048);
        hashSet.add(2048);//唯一性的体现

        hashSet.add(3072);
        hashSet.add(4096);
        hashSet.add(null);
        hashSet.add(null);
        System.out.println(hashSet);
    }
}
