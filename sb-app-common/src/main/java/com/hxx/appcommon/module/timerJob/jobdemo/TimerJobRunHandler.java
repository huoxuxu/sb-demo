package com.hxx.appcommon.module.timerJob.jobdemo;

import com.hxx.appcommon.module.timerJob.TimerJobContext;
import com.hxx.appcommon.module.timerJob.handler.ITimerJobRunHandler;
import com.hxx.sbcommon.common.basic.ComplexPowerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimerJobRunHandler implements ITimerJobRunHandler {

    /**
     * job执行前
     *
     * @param context
     * @return true继续执行，false不执行
     */
    public boolean before(TimerJobContext context) {
        String jobCode = context.getJobCode();
        if (jobCode.equals(Demo3TimerJob.class.getName())) {
            ComplexPowerUtil.everyTenMinuteRun(d -> {
                log.info("不满足before，不执行job：{}", jobCode);
            });
            return false;
        }
        return true;
    }

    public void success(TimerJobContext context) {

    }

    public void fail(TimerJobContext context, Exception error) {

    }

    public void completed(TimerJobContext context) {

    }
}
