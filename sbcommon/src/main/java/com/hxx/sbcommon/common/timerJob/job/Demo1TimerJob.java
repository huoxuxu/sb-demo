package com.hxx.sbcommon.common.timerJob.job;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.timerJob.BaseTimerJob;
import com.hxx.sbcommon.common.timerJob.TimerJobContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Demo1TimerJob extends BaseTimerJob.SimpleTimerJob {

    public Demo1TimerJob() {
        super(Duration.ofSeconds(10));
    }

    /**
     * 执行任务
     *
     * @param context
     */
    @Override
    public void execute(TimerJobContext context) {
        log.info("SimpleTimerJob[{}].execute: {}", this.duration, JsonUtil.toJSON(context));
    }
}
