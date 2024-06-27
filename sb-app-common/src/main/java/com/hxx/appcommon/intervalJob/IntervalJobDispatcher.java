package com.hxx.appcommon.intervalJob;

import com.hxx.springcommon.spring.SpringBeanActivatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 内置线程池驱动的Job调度器
 * 支持系统改时间自动恢复
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-30 13:58:35
 **/
@Slf4j
@Component
public class IntervalJobDispatcher {

    private static final ThreadPoolExecutor threadPool;
    // 内置数据
    private static final Map<String, Object> RUNNING_DATA = new HashMap<>();

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
     * 获取线程池运行信息
     *
     * @return
     */
    public static Map<String, Object> getRunningData() {
        return RUNNING_DATA;
    }

    /**
     * 调度核心方法，由外部驱动
     * 驱动器一般为定时器
     */
    public static void process() {
        log.debug("调度start...");
        Map<String, BaseIntervalJob> beans = SpringBeanActivatorUtil.getBeansOfType(BaseIntervalJob.class);
        log.debug("找到Job个数：{}", Optional.ofNullable(beans).map(d -> d.size()).orElse(0));
        if (MapUtils.isEmpty(beans)) {
            return;
        }

        // 找handlers
        Map<String, BaseIntervalJobExecutorHandler> handlerMap = SpringBeanActivatorUtil.getBeansOfType(BaseIntervalJobExecutorHandler.class);
        List<BaseIntervalJobExecutorHandler> handlers = new ArrayList<>();
        if (handlerMap != null) {
            handlers.addAll(handlerMap.values());
        }
        log.debug("找到Handler个数：{}", handlers.size());
        RUNNING_DATA.put("latestRunning", "{now:" + LocalDateTime.now() + ", jobSize:" + beans.size() + ", handlerSize:" + handlers.size() + "}");

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
        private static final Map<String, LocalDateTime> runMap = new HashMap<>();
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
                    // 判断下次执行的有效性，可能本机时间改变，导致永远不执行
                    next = preRunTime.minusSeconds(3 * task.getIntervalSecond());
                    if (next.isAfter(LocalDateTime.now())) {
                        // 说明上次保存的时间有问题，清空
                        runMap.remove(task.getJobCode());
                        log.warn("InternalIntervalJobHandler，上次保存时间错误，已重置! jobCode={}", task.getJobCode());
                        return true;
                    }
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