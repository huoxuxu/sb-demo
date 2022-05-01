package com.hxx.sbConsole.service.impl.threadPoolDemo;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-01 21:17:02
 **/
@Slf4j
public class GlobalThreadPool {
    /**
     * 核心线程数
     */
    public final static int CORE_POOL_SIZE = 10;

    /**
     * 最大线程数
     */
    public final static int MAX_NUM_POOL_SIZE = 20;

    /**
     * 任务队列大小
     */
    public final static int BLOCKING_QUEUE_SIZE = 30;

    /**
     * 线程池实例
     */
    private final static ThreadPoolExecutor instance = getInstance();


    /**
     * description: 初始化线程池
     *
     * @return: java.util.concurrent.ThreadPoolExecutor
     * @author: weirx
     * @time: 2021/9/10 9:49
     */
    private synchronized static ThreadPoolExecutor getInstance() {
        // 生成线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_NUM_POOL_SIZE,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCKING_QUEUE_SIZE),
                new NamedThreadFactory("Thread-wjbgn-", false));
        return executor;
    }

    private GlobalThreadPool() {
    }

    public static ThreadPoolExecutor getExecutor() {
        return instance;
    }


    public static void main(String[] args) {
        // 创建10个任务，每个任务阻塞10秒
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(10000);
                    System.out.println(new Date() + ":" + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },GlobalThreadPool.getExecutor());
        }

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class NamedThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return null;
        }
    }
}
