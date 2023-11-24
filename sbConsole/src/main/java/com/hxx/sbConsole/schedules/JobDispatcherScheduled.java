package com.hxx.sbConsole.schedules;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.intervalJob.IntervalJobDispatcher;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-31 9:28:03
 **/
@Slf4j
@Component
public class JobDispatcherScheduled {

    @Scheduled(cron = "0/5 * * * * ?")
    public void doDispatch() {
        try {
            // 开启调度
            log.debug("[TP(" + IntervalJobDispatcher.getThreadPool().getQueue().size() + ")]==============loop===============");
            OftenUtil.everyTenMinuteLog(log, log -> {
                Map<String, Object> runningData = IntervalJobDispatcher.getRunningData();
                log.info("IntervalJobDispatcher：{} {} {}", IntervalJobDispatcher.getThreadPool().getQueue().size(), IntervalJobDispatcher.getThreadPool() + "", JsonUtil.toJSON(runningData));
            });
            IntervalJobDispatcher.process();
        } catch (Exception ex) {
            log.error("IntervalJobDispatcher出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

}
