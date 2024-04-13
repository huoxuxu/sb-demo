package com.hxx.appcommon.module.timerJob;

import com.hxx.appcommon.module.timerJob.handler.IJobRunHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
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

    private final List<IJobRunHandler> jobRunHandlers = new ArrayList<>();

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

    public TimerJobDispatcher() {
    }

    /**
     * @param timerJobs 所有待执行的Job
     */
    public TimerJobDispatcher(List<BaseTimerJob> timerJobs) {
        addJobs(timerJobs);

    }

    /**
     * 调度
     */
    public void process() {
        try {
            log.debug("调度start...");
            for (Map.Entry<String, BaseTimerJob> entry : jobs.entrySet()) {
                LocalDateTime now = LocalDateTime.now();
                String jobCode = entry.getKey();
                BaseTimerJob job = entry.getValue();
                TimerJobContext jobContext = runningJobs.get(jobCode);
                if (jobContext != null) {
                    // 如果一个job已经长时间运行，则提示
                    Duration dur = Duration.between(jobContext.getRunTime(), now);
                    if (dur.getSeconds() > WARN_RUN_SECOND) {
                        log.warn("LONG-RUNNING：Job已长时间运行，请及时关注！{} 运行时长：{}s", jobCode, dur.getSeconds());
                    }
                    // job 还在执行中，跳过
                    continue;
                }
                TimerJobContext context = new TimerJobContext(jobCode, job);
                threadPool.submit(() -> {
                    try {
                        // 加入运行中Job集合
                        runningJobs.put(jobCode, context);
                        // 最近执行时间
                        TimerJobContext preJobContext = preJobContexts.getOrDefault(jobCode, null);
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

                        for (IJobRunHandler jobRunHandler : this.jobRunHandlers) {
                            boolean beforeResult = jobRunHandler.before(context);
                            if (!beforeResult) {
                                return;
                            }
                        }
                        try {
                            // job执行
                            job.execute(context);
                            context.setSuccess();
                            for (IJobRunHandler jobRunHandler : this.jobRunHandlers) {
                                jobRunHandler.success(context);
                            }
                        } catch (Exception ex) {
                            log.error("job执行失败：{}", ExceptionUtils.getStackTrace(ex));
                            context.setCompleted(false, ex);
                            for (IJobRunHandler jobRunHandler : this.jobRunHandlers) {
                                jobRunHandler.fail(context, ex);
                            }
                        } finally {
                            // job执行信息
                            preJobContexts.put(jobCode, context);
                            for (IJobRunHandler jobRunHandler : this.jobRunHandlers) {
                                jobRunHandler.completed(context);
                            }
                        }
                    } catch (Exception ex) {
                        log.error("job调度失败：{}", ExceptionUtils.getStackTrace(ex));
                    } finally {
                        // 移除运行中
                        runningJobs.remove(jobCode);
                    }
                });
            }
            log.debug("调度成功！");
        } catch (Throwable th) {
            log.error("调度失败：{}", ExceptionUtils.getStackTrace(th));
        }
    }

    public void addJobs(List<BaseTimerJob> timerJobs) {
        for (BaseTimerJob job : timerJobs) {
            jobs.put(job.getClass().getName(), job);
        }
    }

    public void addJobRunHandler(IJobRunHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler不能为null");
        }
        this.jobRunHandlers.add(handler);
    }

    public String getTPInfo() {
        return threadPool + "";
    }

}
