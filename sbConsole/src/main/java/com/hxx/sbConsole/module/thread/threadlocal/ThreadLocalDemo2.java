package com.hxx.sbConsole.module.thread.threadlocal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-27 10:27:05
 **/
public class ThreadLocalDemo2 {
    private static final ThreadLocal<String> logbackTraceIdTL = new ThreadLocal<>();

    public static void set(String val) {
        System.out.println("线程1: " + logbackTraceIdTL.get());
        logbackTraceIdTL.set(val);
        System.out.println("线程1: " + logbackTraceIdTL.get());
    }

}
