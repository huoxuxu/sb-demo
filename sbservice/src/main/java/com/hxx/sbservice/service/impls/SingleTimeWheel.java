package com.hxx.sbservice.service.impls;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-09-03 11:02:09
 **/

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 单级时间轮
 * 1. 只使用一个时间轮，时间轮包含 59 个 tick，每个 tick 表示 1s 的时间跨度
 * 2. 通过一个指针指示当前时间，指针随时间后移，到达一个位置后将该位置的延时任务执行
 * 3. 指针到达时间轮末尾后，继续从 0 循环开始
 * 4. 如果当前没有任务，则时间轮停止工作，直到有任务添加时唤醒
 */
public class SingleTimeWheel {
    private static final int CAP = 30;
    private static final int TICK = 1000; // 1s
    private final HashedWheelBucket[] wheel = new HashedWheelBucket[CAP];
    private final ThreadFactory threadFactory = (r) -> new Thread(r, "WorkerThread");
    private final ExecutorService executor = Executors.newCachedThreadPool(threadFactory);
    private static final Object lock = new Object();

    /**
     * 时间指针
     */
    private int cur;
    /**
     * 当前的任务数量
     */
    private final AtomicInteger size = new AtomicInteger(0);

    public SingleTimeWheel() {
        for (int i=0; i<CAP; i++) {
            wheel[i] = new HashedWheelBucket();
        }
    }

    /**
     * 添加任务
     * @param task 任务
     * @return 是否添加成功
     */
    public boolean addTask(TimeTask task) {
        if (task == null || task.delayTimeSec > CAP) {
            return false;
        }
        int i = size.get();
        int bucket = (cur + task.delayTimeSec) % CAP;
        synchronized (wheel[bucket].lock) {
            wheel[bucket].offer(task);
        }
        size.incrementAndGet();
        // 确认条件下，再尝试争用锁唤醒时间轮线程
        // 避免因为锁争用导致添加延时任务被推迟
        if (i == 0 || size.get() == 0) {
            synchronized (lock) {
                lock.notify();
            }
        }
        return true;
    }

    /**
     * 开启时间轮
     */
    public void start() {
        new Thread(this::run, "run thread").start();
        System.out.println("SingleTimeWheel start to run");
    }

    public void run() {
        try {
            for (;;) {
                synchronized (lock) {
                    // 为了避免虚假唤醒，这里不能用 if
                    while (size.get() == 0) {
                        lock.wait();
                    }
                    cur++;
                    if (cur == CAP) {
                        // 指针复位
                        cur = 0;
                    }
                    Thread.sleep(TICK);

                    HashedWheelBucket bucket = wheel[cur];
                    System.out.printf("cur: %d, size: %d, bucketSize:%d\n", cur, size.get(), bucket.size);
                    synchronized (bucket.lock) {
                        while (!bucket.isEmpty()) {
                            TimeTask task = bucket.poll();
                            size.decrementAndGet();
                            System.out.println("exec sec: " + cur);
                            executor.execute(task.runnable); // dispatch worker thread to exec task
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("SingleTimeWheel run thread exit, error: " + e);
        } finally {
            executor.shutdown();
        }
    }
}
