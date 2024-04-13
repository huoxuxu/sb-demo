package com.hxx.sbcommon.common.timerJob;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseTimerJob implements Serializable {
    private static final long serialVersionUID = 5987271320362040172L;

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
    public abstract LocalDateTime getNextRunTime(LocalDateTime preExecuteCompletedTime);

}
