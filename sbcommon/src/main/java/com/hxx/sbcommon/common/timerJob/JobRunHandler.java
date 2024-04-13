package com.hxx.sbcommon.common.timerJob;

import com.hxx.sbcommon.common.timerJob.handler.IJobRunHandler;

public class JobRunHandler implements IJobRunHandler {

    /**
     * job执行前
     *
     * @param context
     * @return true继续执行，false不执行
     */
    public boolean before(TimerJobContext context) {
        return true;
    }

    public void success(TimerJobContext context) {

    }

    public void fail(TimerJobContext context, Exception error) {

    }

    public void completed(TimerJobContext context) {

    }
}
