package com.hxx.sbConsole.module.thread.futureTask.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 13:37:42
 **/
@Slf4j
public class MakeTeaExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 创建线程1的FutureTask
        FutureTask<String> ft1 = new FutureTask<String>(new T1Task());
        // 创建线程2的FutureTask
        FutureTask<String> ft2 = new FutureTask<String>(new T2Task());

        executorService.submit(ft1);
        executorService.submit(ft2);

        log.info(ft1.get() + ft2.get());
        log.info("开始泡茶");

        executorService.shutdown();
    }

    static class T1Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            log.info("T1:洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            log.info("T1:烧开水...");
            TimeUnit.SECONDS.sleep(15);

            return "T1:开水已备好";
        }
    }

    static class T2Task implements Callable<String> {
        @Override
        public String call() throws Exception {
            log.info("T2:洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            log.info("T2:洗茶杯...");
            TimeUnit.SECONDS.sleep(2);

            log.info("T2:拿茶叶...");
            TimeUnit.SECONDS.sleep(1);
            return "T2:福鼎白茶拿到了";
        }
    }

}
