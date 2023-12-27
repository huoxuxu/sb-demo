package com.hxx.sbConsole.module.thread.threadlocal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-27 10:27:26
 **/
public class ThreadLocalClient {
    /*
     * 不同的ThreadLocal实例，在同一线程下并不共享数据
     * */
    public static void main(String[] args) {
        ThreadLocalDemo1.set("hello wode");

        System.out.println("ok");
    }
}
