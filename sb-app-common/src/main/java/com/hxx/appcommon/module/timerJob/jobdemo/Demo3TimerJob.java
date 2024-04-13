package com.hxx.appcommon.module.timerJob.jobdemo;

import com.hxx.appcommon.module.timerJob.TimerJobContext;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.appcommon.module.timerJob.SimpleTimerJob;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Demo3TimerJob extends SimpleTimerJob {
    private static final long serialVersionUID = -6589225548587374081L;

    public Demo3TimerJob() {
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
