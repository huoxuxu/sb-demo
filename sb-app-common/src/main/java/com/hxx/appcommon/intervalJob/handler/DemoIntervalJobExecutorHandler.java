package com.hxx.appcommon.intervalJob.handler;

import com.hxx.appcommon.intervalJob.BaseIntervalJob;
import com.hxx.appcommon.intervalJob.BaseIntervalJobExecutorHandler;
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
public class DemoIntervalJobExecutorHandler implements BaseIntervalJobExecutorHandler {

    @Override
    public boolean onTaskSubmit(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
        return true;
    }

    @Override
    public void onTaskSubmitted(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
        log.info("SUBMITED：{}", task);
    }

    @Override
    public void onBeforeRun(BaseIntervalJob.IntervalJobContext context) {
    }

    @Override
    public void onSuccess(BaseIntervalJob.IntervalJobContext context) {
    }

    @Override
    public void onError(BaseIntervalJob.IntervalJobContext context) {
    }

    @Override
    public void onCompleted(BaseIntervalJob task, BaseIntervalJob.IntervalJobContext context) {
        long cost = context.getCostMs();
        if (cost > 999) {
            log.warn("{} 任务执行耗时：{}", task.getJobCode(), cost);
        }
    }
}
