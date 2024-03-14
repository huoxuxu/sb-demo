package com.hxx.sbConsole.module.thread.futureTask;

import java.util.concurrent.Callable;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 11:54:59
 **/
public class Task implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("子线程正在计算");
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += i;
        }
        return sum;
    }
}
