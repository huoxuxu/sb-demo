package com.hxx.sbConsole.module.thread.schedules;

import com.hxx.appcommon.module.timerJob.jobdemo.Demo3TimerJob;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.appcommon.module.timerJob.BaseTimerJob;
import com.hxx.appcommon.module.timerJob.TimerJobDispatcher;
import com.hxx.appcommon.module.timerJob.jobdemo.Demo1TimerJob;
import com.hxx.appcommon.module.timerJob.jobdemo.Demo2TimerJob;
import com.hxx.appcommon.module.timerJob.jobdemo.JobRunHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-31 9:28:03
 **/
@Slf4j
@Component
public class JobDispatcherScheduled implements ApplicationRunner {

    @Autowired
    private TimerJobDispatcher timerJobDispatcher;
    @Autowired
    private JobRunHandler jobRunHandler;

//    @Scheduled(cron = "0/5 * * * * ?")
//    public void doDispatch() {
//        try {
//            // 开启调度
//            log.debug("[TP(" + IntervalJobDispatcher.getThreadPool().getQueue().size() + ")]==============loop===============");
//            OftenUtil.everyTenMinuteLog(log, log -> {
//                Map<String, Object> runningData = IntervalJobDispatcher.getRunningData();
//                log.info("IntervalJobDispatcher：{} {} {}", IntervalJobDispatcher.getThreadPool().getQueue().size(), IntervalJobDispatcher.getThreadPool() + "", JsonUtil.toJSON(runningData));
//            });
//            IntervalJobDispatcher.process();
//        } catch (Exception ex) {
//            log.error("IntervalJobDispatcher出现异常：{}", ExceptionUtils.getStackTrace(ex));
//        }
//    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 处理TimerJobDispatcher
        {
            List<BaseTimerJob> jobs = new ArrayList<>();
            Demo1TimerJob job1 = new Demo1TimerJob();
            jobs.add(job1);
            Demo2TimerJob job2 = new Demo2TimerJob();
            jobs.add(job2);
            Demo3TimerJob job3 = new Demo3TimerJob();
            jobs.add(job3);
            // addJob
            timerJobDispatcher.addJobs(jobs);
            // addJobHandler
            timerJobDispatcher.addJobRunHandler(jobRunHandler);
        }

    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void doDispatch2() {
        try {
            // 开启调度
            log.debug("==============[TimerJob]===============");
            OftenUtil.everyTenMinuteLog(log, log -> {
                String tpInfo = timerJobDispatcher.getTPInfo();
                log.info("[TimerJob]-TP：{}", tpInfo);
            });
            timerJobDispatcher.process();
        } catch (Exception ex) {
            log.error("IntervalJobDispatcher出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

}
