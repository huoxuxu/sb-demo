package com.hxx.sbConsole.module.thread.threadlocal;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 15:41:36
 **/
@Slf4j
public class ThreadLocalDemo3 {
    private static ThreadLocal<String> value = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        try {
            threadLocalTest();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok");
    }

    public static void threadLocalTest() throws Exception {
        // threadlocal线程封闭示例
        value.set("这是主线程设置的123"); // 主线程设置值
        String val = value.get();
        System.out.println("【新线程】执行之前，主线程取到的值：" + val);

        new Thread(() -> {
            String v = value.get();
            System.out.println("【新线程】取到的值：" + v);
            // 设置 threadLocal
            value.set("这是【新线程】设置的456");
            v = value.get();
            System.out.println("重新设置之后，【新线程】取到的值：" + v);
            System.out.println("【新线程】执行结束");
        }).start();

        Thread.sleep(5000L); // 等待所有线程执行结束

        val = value.get();
        System.out.println("【新线程】执行之后，主线程取到的值：" + val);
    }

}
