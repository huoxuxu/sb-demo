package com.hxx.sbConsole.module.thread.threadlocal;

import com.hxx.sbcommon.common.thread.ThreadLocalCloseable;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 15:41:36
 **/
@Slf4j
public class ThreadLocalDemo {
    public static void main(String[] args) throws Exception {
        try {
            try (var threadLocal = new ThreadLocalCloseable()) {
                // 测试 ThreadLocal
                threadLocalTest(threadLocal);
            }
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok");
    }

    public static void threadLocalTest(ThreadLocalCloseable threadLocal) throws Exception {
        String key = "k1";
        // threadlocal线程封闭示例
        threadLocal.set(key, "这是主线程设置的123"); // 主线程设置值
        Object val = threadLocal.getValByKey(key);
        System.out.println("【新线程】执行之前，主线程取到的值：" + val);

        new Thread(() -> {
            Object v = threadLocal.getValByKey(key);
            System.out.println("【新线程】取到的值：" + v);
            // 设置 threadLocal
            threadLocal.set(key, "这是【新线程】设置的456");
            v = threadLocal.get();
            System.out.println("重新设置之后，【新线程】取到的值：" + v);
            System.out.println("【新线程】执行结束");
        }).start();

        Thread.sleep(5000L); // 等待所有线程执行结束

        val = threadLocal.get();
        System.out.println("【新线程】执行之后，主线程取到的值：" + val);
    }

}
