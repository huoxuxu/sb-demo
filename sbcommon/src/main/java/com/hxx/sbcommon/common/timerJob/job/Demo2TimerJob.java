package com.hxx.sbcommon.common.timerJob.job;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.timerJob.BaseTimerJob;
import com.hxx.sbcommon.common.timerJob.TimerJobContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Demo2TimerJob extends BaseTimerJob.SimpleTimerJob {

    public Demo2TimerJob() {
        super(Duration.ofSeconds(20));
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
