package com.hxx.sbcommon.common.intervalJob;

import com.hxx.sbcommon.common.intervalJob.BaseIntervalJob;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 调度处理器
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-07-27 10:46:05
 **/
public interface BaseIntervalJobExecutorHandler {

    /**
     * 任务提交到线程池前调用
     *
     * @param threadPool
     * @return true可以运行，false不运行
     */
    boolean onTaskSubmit(BaseIntervalJob task, ThreadPoolExecutor threadPool);

    /**
     * 任务提交线程池后调用
     *
     * @param task
     * @param threadPool
     */
    void onTaskSubmitted(BaseIntervalJob task, ThreadPoolExecutor threadPool);

    /**
     * 任务被线程池中的线程开始运行时调用
     *
     * @param context
     */
    void onBeforeRun(BaseIntervalJob.IntervalJobContext context);

    /**
     * 任务运行成功时调用
     *
     * @param context
     */
    void onSuccess(BaseIntervalJob.IntervalJobContext context);

    /**
     * 任务运行失败时调用
     *
     * @param context
     */
    void onError(BaseIntervalJob.IntervalJobContext context);

    /**
     * 任务运行完成时调用，运行结果可能成功或失败，可通过上下文中的Ok 判断是否运行成功
     *
     * @param context
     */
    void onCompleted(BaseIntervalJob task, BaseIntervalJob.IntervalJobContext context);

}
