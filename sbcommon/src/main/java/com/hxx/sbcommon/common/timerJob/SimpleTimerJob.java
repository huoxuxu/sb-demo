package com.hxx.sbcommon.common.timerJob;


import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public abstract class SimpleTimerJob extends BaseTimerJob {
    /**
     * 间隔
     */
    protected Duration duration;

    protected SimpleTimerJob(Duration duration) {
        this.duration = duration;
    }

    /**
     * 执行任务
     *
     * @param context
     */
    public abstract void execute(TimerJobContext context);

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
        LocalDateTime next = preExecuteCompletedTime.plus(this.duration);
        return next;
    }

}
