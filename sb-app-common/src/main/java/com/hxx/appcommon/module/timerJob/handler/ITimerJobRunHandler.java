package com.hxx.appcommon.module.timerJob.handler;

import com.hxx.appcommon.module.timerJob.TimerJobContext;

public interface ITimerJobRunHandler {

    /**
     * job执行前
     *
     * @param context
     * @return true继续执行，false不执行
     */
    boolean before(TimerJobContext context);

    /**
     * job执行成功
     *
     * @param context
     */
    void success(TimerJobContext context);

    /**
     * job执行失败
     *
     * @param context
     */
    void fail(TimerJobContext context, Exception error);

    /**
     * job执行完成，执行成功或失败后会执行此方法
     *
     * @param context
     */
    void completed(TimerJobContext context);

}
