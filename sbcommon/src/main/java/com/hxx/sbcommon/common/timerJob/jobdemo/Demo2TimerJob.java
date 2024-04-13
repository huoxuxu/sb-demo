package com.hxx.sbcommon.common.timerJob.jobdemo;

import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.timerJob.SimpleTimerJob;
import com.hxx.sbcommon.common.timerJob.TimerJobContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class Demo2TimerJob extends SimpleTimerJob {
    private static final long serialVersionUID = -6589225548587374081L;

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
