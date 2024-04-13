package com.hxx.sbcommon.common.timerJob;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public abstract class CronTimerJob extends BaseTimerJob {

    /**
     * cron表达式
     */
    private String cron;

    public CronTimerJob(String cron) {
        this.cron = cron;
    }

    /**
     * 执行任务
     *
     * @param context
     */
    @Override
    public void execute(TimerJobContext context) {

    }

    /**
     * 获取下次运行时间
     *
     * @param preExecuteCompletedTime 上次运行完成时间
     * @return
     */
    @Override
    public LocalDateTime getNextRunTime(LocalDateTime preExecuteCompletedTime) {
        if (preExecuteCompletedTime == null || preExecuteCompletedTime.isEqual(LocalDateTime.MIN)) {
            return LocalDateTime.now();
        }
        // 使用cron驱动获取下次执行时间
        return null;
    }

}
