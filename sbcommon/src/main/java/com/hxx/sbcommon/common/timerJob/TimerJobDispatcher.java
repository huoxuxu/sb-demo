package com.hxx.sbcommon.common.timerJob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class TimerJobDispatcher {

    // job长时间（如果超过n 秒）运行则日志提示
    private static final int WARN_RUN_SECOND = 5 * 60;
    private static final ThreadPoolExecutor threadPool;

    // 存储所有job,k=jobClsName v=定时任务
    private static final ConcurrentHashMap<String, BaseTimerJob> jobs = new ConcurrentHashMap<>();
    // 存储job的上次运行信息
    private static final ConcurrentHashMap<String, TimerJobContext> preJobContexts = new ConcurrentHashMap<>();
    // 运行中的job
    private static final ConcurrentHashMap<String, TimerJobContext> runningJobs = new ConcurrentHashMap<>();

    static {
        // 创建一个线程池对象
        threadPool = new ThreadPoolExecutor(
                8, // 核心线程数
                16, // 最大线程数
                60, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(500), // 工作队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
    }

    /**
     * @param timerJobs 所有待执行的Job
     */
    public TimerJobDispatcher(List<BaseTimerJob> timerJobs) {
        for (BaseTimerJob job : timerJobs) {
            jobs.put(job.getClass().getName(), job);
        }

    }

    /**
     * 调度
     */
    public void process() {
        try {
            log.debug("调度start...");
            for (Map.Entry<String, BaseTimerJob> entry : jobs.entrySet()) {
                LocalDateTime now = LocalDateTime.now();
                String jobName = entry.getKey();
                BaseTimerJob job = entry.getValue();
                TimerJobContext jobContext = runningJobs.get(jobName);
                if (jobContext != null) {
                    // 如果一个job已经长时间运行，则提示
                    Duration dur = Duration.between(jobContext.getRunTime(), now);
                    if (dur.getSeconds() > WARN_RUN_SECOND) {
                        log.warn("LONG-RUNNING：Job已长时间运行，请及时关注！{} 运行时长：{}s", jobName, dur.getSeconds());
                    }
                    // job 还在执行中，跳过
                    continue;
                }
                TimerJobContext context = new TimerJobContext(jobName, job);
                threadPool.submit(() -> {
                    try {
                        // 加入运行中Job集合
                        runningJobs.put(jobName, context);
                        // 最近执行时间
                        TimerJobContext preJobContext = preJobContexts.getOrDefault(jobName, null);
                        LocalDateTime preCompletedTime = Optional.ofNullable(preJobContext).map(d -> d.getCompleteTime()).orElse(null);
                        if (preCompletedTime != null && preCompletedTime.isAfter(now)) {
                            preCompletedTime = now;
                        }
                        LocalDateTime nextRunTime = job.getNextRunTime(preCompletedTime);
                        // 判断是否可执行
                        if (nextRunTime.isAfter(LocalDateTime.now())) {
                            // 时间未到，不可执行，跳过
                            return;
                        }

                        try {
                            // job执行
                            job.execute(context);
                            context.setSuccess();
                        } catch (Exception ex) {
                            log.error("job执行失败：{}", ExceptionUtils.getStackTrace(ex));
                            context.setCompleted(false, ex);
                        } finally {
                            // job执行信息
                            preJobContexts.put(jobName, context);
                        }
                    } catch (Exception ex) {
                        log.error("job调度失败：{}", ExceptionUtils.getStackTrace(ex));
                    } finally {
                        // 移除运行中
                        runningJobs.remove(jobName);
                    }
                });
            }
            log.debug("调度成功！");
        } catch (Throwable th) {
            log.error("调度失败：{}", ExceptionUtils.getStackTrace(th));
        }
    }

    public String getTPInfo() {
        return threadPool + "";
    }

}
