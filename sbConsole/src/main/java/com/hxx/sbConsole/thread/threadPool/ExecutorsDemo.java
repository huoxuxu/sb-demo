package com.hxx.sbConsole.thread.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 10:37:01
 **/
@Slf4j
public class ExecutorsDemo {

    public static void main(String... args) {
        int cpuCoreCount = Runtime.getRuntime()
                .availableProcessors();
        AThreadFactory threadFactory = new AThreadFactory();
        ARunnable runnanle = new ARunnable();
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(cpuCoreCount, threadFactory);
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(threadFactory);
        ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(cpuCoreCount, threadFactory);
        ScheduledExecutorService singleThreadExecutor = Executors.newSingleThreadScheduledExecutor(threadFactory);
        fixedThreadPool.submit(runnanle);
        cachedThreadPool.submit(runnanle);
        newScheduledThreadPool.scheduleAtFixedRate(runnanle, 0, 1, TimeUnit.SECONDS);
        singleThreadExecutor.scheduleWithFixedDelay(runnanle, 0, 100, TimeUnit.MILLISECONDS);

        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fixedThreadPool.shutdownNow();
        cachedThreadPool.shutdownNow();
        newScheduledThreadPool.shutdownNow();
        singleThreadExecutor.shutdownNow();
    }

    static class AThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread("aThread-" + threadNumber.incrementAndGet());
        }
    }

    static class ARunnable implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Current Thread Name:" + Thread.currentThread()
                    .getName());
        }
    }
}
