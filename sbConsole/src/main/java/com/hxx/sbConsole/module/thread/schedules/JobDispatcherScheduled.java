package com.hxx.sbConsole.module.thread.schedules;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.intervalJob.IntervalJobDispatcher;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.timerJob.BaseTimerJob;
import com.hxx.sbcommon.common.timerJob.TimerJobDispatcher;
import com.hxx.sbcommon.common.timerJob.job.Demo1TimerJob;
import com.hxx.sbcommon.common.timerJob.job.Demo2TimerJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-31 9:28:03
 **/
@Slf4j
@Component
public class JobDispatcherScheduled {

    @Autowired
    private TimerJobDispatcher timerJobDispatcher;

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

    @Bean("TimerJobDispatcher")
    public TimerJobDispatcher timerJobDispatcher() {
        List<BaseTimerJob> jobs = new ArrayList<>();
        Demo1TimerJob job1 = new Demo1TimerJob();
        jobs.add(job1);
        Demo2TimerJob job2 = new Demo2TimerJob();
//        jobs.add(job2);
        return new TimerJobDispatcher(jobs);
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
