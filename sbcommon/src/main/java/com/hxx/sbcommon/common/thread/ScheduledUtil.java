package com.hxx.sbcommon.common.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-20 8:56:50
 **/
public class ScheduledUtil {


    /**
     * 创建定时调度服务
     *
     * @param threadName
     * @return
     */
    public static ScheduledExecutorService create(String threadName) {
        return Executors.newScheduledThreadPool(5, r -> new Thread(r, threadName));
    }

    /**
     * 创建并运行定时任务
     *
     * @param threadName 线程名
     * @param interval   任务执行的间隔，毫秒
     * @param runnable   任务
     * @return
     */
    public static void setRunMethod(String threadName, int interval, Runnable runnable) {
        ScheduledExecutorService scheduledExecutorService = create(threadName);
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * @param scheduledExecutorService
     * @param interval                 任务执行的间隔，毫秒
     * @param runnable
     */
    public static void setRunMethod(ScheduledExecutorService scheduledExecutorService, int interval, Runnable runnable) {
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, interval, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭executor
     *
     * @param scheduledExecutorService
     */
    public static void shutdown(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.shutdown();
    }

}
