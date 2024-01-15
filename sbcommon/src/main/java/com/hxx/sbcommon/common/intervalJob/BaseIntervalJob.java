package com.hxx.sbcommon.common.intervalJob;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 所有间隔运行的job继承此类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-30 14:24:52
 **/
@Slf4j
public abstract class BaseIntervalJob implements Runnable {
    protected String jobCode;

    // 间隔，秒
    private final long intervalSecond;

    // Task是否运行
    private volatile boolean running;

    // 任务是否已提交到线程池
    private volatile boolean submitted;

    private LocalDateTime startRunTime = LocalDateTime.MIN;

    private List<BaseIntervalJobExecutorHandler> handlers = new ArrayList<>();

    public BaseIntervalJob(long intervalSecond) {
        UUID uuid = UUID.randomUUID();
        this.jobCode = this.getClass().getTypeName() + "-" + uuid;
        this.intervalSecond = intervalSecond;
    }

    /**
     * 获取jobCode
     *
     * @return
     */
    public String getJobCode() {
        return this.jobCode;
    }

    /**
     * 获取此Job运行间隔
     *
     * @return
     */
    public long getIntervalSecond() {
        return intervalSecond;
    }

    /**
     * job是否运行中
     *
     * @return
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * 获取job运行开始时间
     *
     * @return
     */
    public LocalDateTime getStartRunTime() {
        return this.startRunTime;
    }

    /**
     * 获取任务是否已提交到线程池
     *
     * @return
     */
    public boolean isSubmitted() {
        return submitted;
    }

    /**
     * 设置任务是否已提交到线程池
     *
     * @param submitted
     */
    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    /**
     * 设置处理器
     *
     * @param handlers
     */
    public void setHandlers(List<BaseIntervalJobExecutorHandler> handlers) {
        this.handlers = handlers;
    }

    /**
     * 获取Job运行时长
     *
     * @return
     */
    public long getRunningSecond() {
        if (this.startRunTime.isEqual(LocalDateTime.MIN)) {
            return 0;
        }

        Duration dur = Duration.between(this.startRunTime, LocalDateTime.now());
        return dur.getSeconds();
    }

    @Override
    public String toString() {
        String durSecondsStr = "";
        long runningSecond = this.getRunningSecond();
        if (runningSecond > 0) {
            durSecondsStr = "开始运行：" + this.startRunTime + "已耗时：" + runningSecond + "s";
        }

        return "[" + this.jobCode + "] " + durSecondsStr + " " + (this.running ? "RUNNING" : "") + " " + (this.submitted ? "SUBMITTED" : "");
    }

    /**
     * Task的run方法，内部不会抛出异常
     * 不能实现此方法
     */
    @Override
    public final void run() {
        IntervalJobContext context = new IntervalJobContext(this);
        try {
            running = true;
            startRunTime = LocalDateTime.now();
            for (BaseIntervalJobExecutorHandler handler : this.handlers) {
                handler.onBeforeRun(context);
            }

            // 开始执行
            process(context);
            // 设置运行结果
            context.setComplete(true);
            for (BaseIntervalJobExecutorHandler handler : this.handlers) {
                handler.onSuccess(context);
            }
        } catch (Exception e) {
            context.setComplete(false, e);
            try {
                for (BaseIntervalJobExecutorHandler handler : this.handlers) {
                    handler.onError(context);
                }
            } catch (Exception e1) {
                log.error("handler.onError出现异常：{}", ExceptionUtils.getStackTrace(e1));
            }
        } finally {
            running = false;
            startRunTime = LocalDateTime.MIN;
            submitted = false;
            try {
                for (BaseIntervalJobExecutorHandler handler : this.handlers) {
                    handler.onCompleted(this, context);
                }
            } catch (Exception e1) {
                log.error("handler.onCompleted出现异常：{}", ExceptionUtils.getStackTrace(e1));
            }
        }
    }

    /**
     * Job执行逻辑
     *
     * @param context job执行上下文
     */
    public abstract void process(IntervalJobContext context);

    /**
     * job运行上下文
     */
    @Data
    public static class IntervalJobContext {

        private BaseIntervalJob job;
        /**
         * 开始运行时间
         */
        private LocalDateTime startTime;

        private long costMs;

        private boolean ok;
        private Exception error;

        private Map<String, Object> data = new HashMap<>();

        public IntervalJobContext(BaseIntervalJob job) {
            this.job = job;
            this.startTime = LocalDateTime.now();
        }

        public void setComplete(boolean ok) {
            this.ok = ok;
            LocalDateTime now = LocalDateTime.now();
            Duration dur = Duration.between(startTime, now);
            this.costMs = dur.toMillis();
        }

        public void setComplete(boolean ok, Exception err) {
            setComplete(ok);
            this.error = err;
        }
    }
}
