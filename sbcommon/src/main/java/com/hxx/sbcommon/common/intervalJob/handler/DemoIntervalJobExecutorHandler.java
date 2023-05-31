package com.hxx.sbcommon.common.intervalJob.handler;

import com.hxx.sbcommon.common.intervalJob.BaseIntervalJob;
import com.hxx.sbcommon.common.intervalJob.IntervalJobDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-30 19:10:55
 **/
@Slf4j
@Component
public class DemoIntervalJobExecutorHandler extends IntervalJobDispatcher.IntervalJobExecutorHandler {

    @Override
    public boolean onTaskSubmit(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
        return super.onTaskSubmit(task, threadPool);
    }

    @Override
    public void onTaskSubmitted(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
        super.onTaskSubmitted(task, threadPool);
        log.info("SUBMITED：[job-" + task.getJobCode() + "] [" + (task.isRunning() ? "RUNNING" : "") + "] [" + (task.isSubmitted() ? "SUBMITTED" : "") + "]");
    }

    @Override
    public void onBeforeRun(BaseIntervalJob.IntervalJobContext context) {
        super.onBeforeRun(context);
    }

    @Override
    public void onSuccess(BaseIntervalJob.IntervalJobContext context) {
        super.onSuccess(context);
    }

    @Override
    public void onError(BaseIntervalJob.IntervalJobContext context) {
        super.onError(context);
    }

    @Override
    public void onCompleted(BaseIntervalJob task, BaseIntervalJob.IntervalJobContext context) {
        super.onCompleted(task, context);

        long cost = context.getCostMs();
        if (cost > 999) {
            log.warn("{} 任务执行耗时：{}", task.getJobCode(), cost);
        }
    }
}
