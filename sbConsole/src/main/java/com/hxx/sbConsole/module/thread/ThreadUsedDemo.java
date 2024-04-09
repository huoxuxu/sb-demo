package com.hxx.sbConsole.module.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 15:12:10
 **/
@Slf4j
public class ThreadUsedDemo {

    public static void demo() throws Exception {
        // Thread类的join方法它会让主线程等待子线程运行结束后，才能继续运行
        {
            Thread thread1 = new Thread(() -> System.out.println("a"));
            Thread thread2 = new Thread(() -> System.out.println("b"));
            Thread thread3 = new Thread(() -> System.out.println("c"));

            thread1.start();
            thread1.join();
            thread2.start();
            thread2.join();
            thread3.start();
        }


    }

    public static void main(String[] args) {
        try {
            demo();
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok");
    }




}
