package com.hxx.sbConsole.module.thread.schedules;

import com.hxx.appcommon.module.timerJob.jobdemo.*;
import com.hxx.sbcommon.common.basic.ComplexUtil;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.appcommon.module.timerJob.BaseTimerJob;
import com.hxx.appcommon.module.timerJob.TimerJobDispatcher;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            Demo4TimerJob job4 = new Demo4TimerJob();
            jobs.add(job4);
            // addJob
            timerJobDispatcher.addJobs(jobs);
            // addJobHandler
            timerJobDispatcher.addJobRunHandler(jobRunHandler);
        }
        {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
            // 设定任务在初始延迟0毫秒后开始，然后每300毫秒执行一次, 上个任务执行完成，才会执行下一次
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                try {
                    // 开启调度
                    ComplexUtil.everyTenMinuteRun(d -> {
                        log.debug("==============[TimerJob]===============");
                        String tpInfo = timerJobDispatcher.getTPInfo();
                        log.info("[TimerJob]-TP：{}", tpInfo);
                    });
                    timerJobDispatcher.process();
                } catch (Exception ex) {
                    log.error("IntervalJobDispatcher出现异常：{}", ExceptionUtils.getStackTrace(ex));
                }
            }, 0, 300, TimeUnit.MILLISECONDS);
        }
    }

}
