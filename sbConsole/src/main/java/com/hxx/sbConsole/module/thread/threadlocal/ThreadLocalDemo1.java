package com.hxx.sbConsole.module.thread.threadlocal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-27 10:27:05
 **/
public class ThreadLocalDemo1 {
    private static final ThreadLocal<String> logbackTraceIdTL = new ThreadLocal<>();

    public static void set(String val) {
        Thread thread1 = new Thread(() -> {
            logbackTraceIdTL.set(val);
            System.out.println("线程1: " + logbackTraceIdTL.get());

            ThreadLocalDemo2.set("11223");
            logbackTraceIdTL.remove();
            System.out.println("线程1: " + logbackTraceIdTL.get());

        });
        thread1.start();
    }
}
