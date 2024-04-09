package com.hxx.sbConsole.module.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 15:02:24
 **/
@Slf4j
public class ThreadDemo {

    public static void demo() {
        try {
            // 新开线程-方式1 继承Thread (没有参数、无法获取返回值、无法抛出异常)
            {
                ThreadIns threadIns = new ThreadIns();
                threadIns.start();
            }
            // 新开线程-方式2 实现Runnable传入Thread构造 (没有参数、无法获取返回值、无法抛出异常)
            {
                Thread t = new Thread(new RunnableIns());
                t.start();
            }
            {
                new Thread(() -> System.out.println("123321")).start();
            }
            // 新开线程-方式3 使用FutureTask
            {
                CallableIns call = new CallableIns();
                FutureTask<String> futureTask = new FutureTask<>(call);
                Thread thread = new Thread(futureTask);
                thread.start();
                try {
                    int maxRunTime = 800;
                    long start = System.currentTimeMillis();
                    while (!futureTask.isDone()) {
                        if (System.currentTimeMillis() - start > maxRunTime) {
                            // 取消任务执行
                            futureTask.cancel(true);
                        }

                        System.out.println("FutureTask is Running...");
                        Thread.sleep(500);
                    }

                    if (futureTask.isCancelled()) {
                        System.out.println("任务已被取消！");
                    } else {
                        String result = futureTask.get();
                        System.out.println("Result:" + result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //  线程池
            {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                // 使用 Callable ，可以获取返回值
                Callable<String> callable = () -> {
                    log.info("进入 Callable 的 call 方法");
                    // 模拟子线程任务，在此睡眠 2s，
                    // 小细节：由于 call 方法会抛出 Exception，这里不用像使用 Runnable 的run 方法那样 try/catch 了
                    Thread.sleep(5000);
                    return "Hello from Callable";
                };

                log.info("提交 Callable 到线程池");
                Future<String> future = executorService.submit(callable);

                log.info("主线程继续执行");

                log.info("主线程等待获取 Future 结果");
                // Future.get() blocks until the result is available
                // 阻塞线程直到任务完成
                String result = future.get();
                log.info("主线程获取到 Future 结果: {}", result);

                executorService.shutdown();
            }
            // CompletableFuture
            {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                CompletableFuture<Boolean> completeTask = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return Boolean.TRUE;
                }, executor);
                Boolean val = completeTask.get(5, TimeUnit.SECONDS);
                System.out.println(val);
            }
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    static class ThreadIns extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("Current Thread Name:" + Thread.currentThread()
                        .getName());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class RunnableIns implements Runnable {

        @Override
        public void run() {
            try {
                System.out.println("Current Thread Name2:" + Thread.currentThread()
                        .getName());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class CallableIns implements Callable<String> {
        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            return "Thread-Name:" + Thread.currentThread()
                    .getName();
        }
    }

}
