package com.hxx.sbcommon.common.intervalJob;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
                new LinkedBlockingQueue<>(500), // 工作队列
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
    }

    @Bean
    public InternalIntervalJobHandler getInternalIntervalJobHandler() {
        log.info("创建Bean：InternalIntervalJobHandler");
        return new InternalIntervalJobHandler();
    }

    /**
     * 获取线程池实例
     *
     * @return
     */
    public static ThreadPoolExecutor getThreadPool() {
        return threadPool;
    }

    /**
     * 调度核心方法，由外部驱动
     * 驱动器一般为定时器
     */
    public static void process() {
        log.debug("调度start...");
        Map<String, BaseIntervalJob> beans = applicationContext.getBeansOfType(BaseIntervalJob.class);
        log.debug("找到Job个数：{}", beans == null ? 0 : beans.size());
        if (beans == null) return;

        // 找handlers
        Map<String, BaseIntervalJobExecutorHandler> handlerMap = applicationContext.getBeansOfType(BaseIntervalJobExecutorHandler.class);
        List<BaseIntervalJobExecutorHandler> handlers = new ArrayList<>();
        if (handlerMap != null) {
            handlers.addAll(handlerMap.values());
        }
        log.debug("找到Handler个数：{}", handlers.size());

        // loop
        for (Map.Entry<String, BaseIntervalJob> entry : beans.entrySet()) {
            BaseIntervalJob task = entry.getValue();
            // DEBUG
//            if (!task.jobCode.contains("Demo5IntervalJob")) continue;
            task.setHandlers(handlers);

            boolean canRunFlag = true;
//            handlers.stream().anyMatch(d->!d.onTaskSubmit(task,threadPool));
            for (BaseIntervalJobExecutorHandler handler : handlers) {
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
            for (BaseIntervalJobExecutorHandler handler : handlers) {
                handler.onTaskSubmitted(task, threadPool);
            }
        }
        log.debug("调度成功！");
    }

    /**
     * 内置的处理器，用于实现job间隔运行
     *
     * @Author: huoxuxu
     * @Description:
     * @Date: 2023-05-31 11:28:08
     **/
    @Slf4j
    static class InternalIntervalJobHandler implements BaseIntervalJobExecutorHandler {
        // job运行map，key=job val=job上次运行完成时间
        private static Map<String, LocalDateTime> runMap = new HashMap<>();
        // 如果超过n 秒，则警告
        private static final int WARN_RUN_SECOND = 5 * 60;


        @Override
        public boolean onTaskSubmit(BaseIntervalJob task, ThreadPoolExecutor threadPool) {
            if (task.isRunning() || task.isSubmitted()) {
                log.debug("SKIP-RUNORSUBMIT：{}", task);
                // 如果一个job已经长时间运行，则提示
                long durSeconds = task.getRunningSecond();
                if (durSeconds > WARN_RUN_SECOND) {
                    log.warn("LONG-RUNNING：Job已长时间运行，请及时关注！{}", task);
                }
                return false;
            }

            // 判断是否可以执行
            // 上次成功执行的时间
            LocalDateTime preRunTime = runMap.getOrDefault(task.getJobCode(), null);
            if (preRunTime != null) {
                LocalDateTime next = preRunTime.plusSeconds(task.getIntervalSecond());
                if (next.isAfter(LocalDateTime.now())) {
                    // 不可执行
                    log.debug("SKIP-TIME：{}", task);
                    return false;
                }
            }
            return true;
        }

        @Override
        public void onTaskSubmitted(BaseIntervalJob task, ThreadPoolExecutor threadPool) {

        }

        @Override
        public void onBeforeRun(BaseIntervalJob.IntervalJobContext context) {
        }

        @Override
        public void onSuccess(BaseIntervalJob.IntervalJobContext context) {
        }

        @Override
        public void onError(BaseIntervalJob.IntervalJobContext context) {
            BaseIntervalJob job = context.getJob();
            Exception error = context.getError();
            if (error != null)
                log.error("{},出现异常：{}", job.getJobCode(), ExceptionUtils.getStackTrace(context.getError()));
            else
                log.error("{},出现异常,无具体异常信息!", job.getJobCode());
        }

        @Override
        public void onCompleted(BaseIntervalJob task, BaseIntervalJob.IntervalJobContext context) {
            runMap.put(task.getJobCode(), LocalDateTime.now());
        }

    }
}