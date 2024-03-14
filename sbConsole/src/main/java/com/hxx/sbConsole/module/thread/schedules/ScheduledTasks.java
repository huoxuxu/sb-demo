package com.hxx.sbConsole.module.thread.schedules;

import com.hxx.sbcommon.common.intervalJob.IntervalJobDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-06 16:15:17
 **/
@Slf4j
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks {
    /*
     * 默认的 ConcurrentTaskScheduler 计划执行器采用Executors.newSingleThreadScheduledExecutor() 实现单线程的执行器。
     * 因此，对同一个调度任务的执行总是同一个线程。
     * 如果任务的执行时间超过该任务的下一次执行时间，则会出现任务丢失，跳过该段时间的任务
     *
     * 采用异步的方式执行调度任务，
     * 配置 Spring 的 @EnableAsync，在执行定时任务的方法上标注 @Async配置任务执行池，线程池大小 n 的数量为 单个任务执行所需时间 / 任务执行的间隔时间
     * */
    // 每 3 秒执行一次
    @Scheduled(fixedRate = 1000 * 3)
    public void doTask() {
        try {
            System.out.println("[fixedRate]: now " + LocalDateTime.now());
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    // 本次执行完成后，再过 3 秒执行一次
    @Scheduled(fixedDelay = 1000 * 3)
    public void doTask2() {
        try {
            System.out.println("[fixedDelay]: now " + LocalDateTime.now());
            Thread.sleep(5000);
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    //在固定时间执行
    // 一分钟执行一次
    @Scheduled(cron = "0 */1 *  * * * ")
    public void reportCurrentByCron() {
        try {
            System.out.println("[Cron]: now " + LocalDateTime.now());
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }


}
