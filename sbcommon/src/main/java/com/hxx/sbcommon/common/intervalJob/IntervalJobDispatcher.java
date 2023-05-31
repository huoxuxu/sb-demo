package com.hxx.sbcommon.common.intervalJob;

import com.hxx.sbcommon.common.intervalJob.job.DemoIntervalJob;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-30 13:58:35
 **/
@Slf4j
@Component
public class IntervalJobDispatcher implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static final ThreadPoolExecutor threadPool;

    static {
        // 创建一个线程池对象
        threadPool = new ThreadPoolExecutor(
                8, // 核心线程数
                16, // 最大线程数
                60, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingQueue<>(3000), // 工作队列
                Executors.defaultThreadFactory(), // 线程工厂
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
    }

    /**
     * 获取ApplicationContext
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IntervalJobDispatcher.applicationContext = applicationContext;
//        // 从容器中获取所有实现了XX接口的bean
//        Map<String, BaseIntervalJob> beans = applicationContext.getBeansOfType(BaseIntervalJob.class);
//        log.info("找到Job个数：{}", beans.size());
//        tls.addAll(beans.values());
        // 添加handlers

    }

    /**
     * 获取线程池实例
     *
     * @return
     */
    public static ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

//    public static List<IntervalJobExecutorHandler> getHandlers() {
//        return handlers;
//    }
//
//    public static void addHandlers(IntervalJobExecutorHandler handler) {
//        handlers.add(handler);
//    }


    /**
     * 调度核心方法，由外部驱动
     * 驱动器一般为定时器
     */
    public static void process() {
        log.debug("调度start...");
        Map<String, BaseIntervalJob> beans = applicationContext.getBeansOfType(BaseIntervalJob.class);
        log.info("找到Job个数：{}", beans == null ? 0 : beans.size());
        if (beans == null) return;

        // 找handlers
        Map<String, IntervalJobExecutorHandler> handlerMap = applicationContext.getBeansOfType(IntervalJobExecutorHandler.class);
        List<IntervalJobExecutorHandler> handlers = new ArrayList<>();
        if (handlerMap != null) {
            handlers.addAll(handlerMap.values());
        }
        log.info("找到Handler个数：{}", handlers.size());

        // loop
        for (Map.Entry<String, BaseIntervalJob> entry : beans.entrySet()) {
            BaseIntervalJob task = entry.getValue();
            // DEBUG
//            if (!task.jobCode.contains("Demo5IntervalJob")) continue;
            task.setHandlers(handlers);

            boolean canRunFlag = true;
            for (IntervalJobExecutorHandler handler : handlers) {
                boolean canRun = handler.onTaskSubmit(task, threadPool);
                if (!canRun) {
                    canRunFlag = false;
                    break;
                }
            }
            if (!canRunFlag) {
                continue;
            }

            threadPool.submit(task);
            task.setSubmitted(true);
            for (IntervalJobExecutorHandler handler : handlers) {
                handler.onTaskSubmitted(task, threadPool);
            }
        }
        log.debug("调度成功！");
    }

    /**
     * 调度处理器
     */
    @Slf4j
    public static abstract class IntervalJobExecutorHandler {
        // job运行map，key=job val=job上次运行完成时间
        private static Map<String, LocalDateTime> runMap = new HashMap<>();

        /**
         * 任务提交到线程池前调用，子类实现时，必须优先调用父类此方法，获取Job是否满足运行条件
         *
         * @param threadPool
         * @return true可以运行，false不运行
         */
        public boolean onTaskSubmit(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
            if (task.isRunning() || task.isSubmitted()) {
                log.debug("SKIP-RUNORSUBMIT：[job-" + task.getJobCode() + "] [" + (task.isRunning() ? "RUNNING" : "") + "] [" + (task.isSubmitted() ? "SUBMITTED" : "") + "]");
                return false;
            }

            // 判断是否可以执行
            // 上次成功执行的时间
            LocalDateTime preRunTime = runMap.getOrDefault(task.getJobCode(), null);
            if (preRunTime != null) {
                LocalDateTime next = preRunTime.plusSeconds(task.getIntervalSecond());
                if (next.isAfter(LocalDateTime.now())) {
                    // 不可执行
                    log.debug("SKIP-TIME：[job-" + task.getJobCode() + "] [" + (task.isRunning() ? "RUNNING" : "") + "] [" + (task.isSubmitted() ? "SUBMITTED" : "") + "]");
                    return false;
                }
            }
            return true;
        }

        /**
         * 任务提交线程池后调用
         *
         * @param task
         * @param threadPool
         */
        public void onTaskSubmitted(BaseIntervalJob task, ThreadPoolExecutor threadPool) {

        }

        /**
         * 任务被线程池中的线程开始运行时调用
         *
         * @param context
         */
        public void onBeforeRun(BaseIntervalJob.IntervalJobContext context) {

        }

        /**
         * 任务运行成功时调用
         *
         * @param context
         */
        public void onSuccess(BaseIntervalJob.IntervalJobContext context) {

        }

        /**
         * 任务运行失败时调用
         *
         * @param context
         */
        public void onError(BaseIntervalJob.IntervalJobContext context) {
            BaseIntervalJob job = context.getJob();
            Exception error = context.getError();
            if (error != null)
                log.error("{},出现异常：{}", job.getJobCode(), ExceptionUtils.getStackTrace(context.getError()));
            else
                log.error("{},出现异常,无具体异常信息!", job.getJobCode());
        }

        /**
         * 任务运行完成时调用，运行结果可能成功或失败，可通过上下文中的Ok 判断是否运行成功
         *
         * @param context
         */
        public void onCompleted(BaseIntervalJob task, BaseIntervalJob.IntervalJobContext context) {
            runMap.put(task.getJobCode(), LocalDateTime.now());
        }

    }
}
