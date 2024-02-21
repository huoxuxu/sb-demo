package com.hxx.sbcommon.common.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-20 8:56:50
 **/
class TimerUtil {

    /**
     * 创建并运行定时任务
     *
     * @param threadName 线程名
     * @param interval   任务执行的间隔，秒
     * @param runnable   任务
     * @return
     */
    ScheduledExecutorService createAndRun(String threadName, int interval, Runnable runnable) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, r -> new Thread(r, threadName));
        scheduledExecutorService.scheduleAtFixedRate(runnable, 0, interval, TimeUnit.SECONDS);
        return scheduledExecutorService;
    }

    /**
     * 关闭executor
     *
     * @param scheduledExecutorService
     */
    void shutdown(ScheduledExecutorService scheduledExecutorService) {
        scheduledExecutorService.shutdown();
    }

}
