package com.hxx.sbConsole.commons.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 17:39:06
 **/
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    // 线程池的最大线程数
    private int corePoolSize = 50;
    // 线程池的核心线程数
    private int maxPoolSize = 200;
    // 线程池的队列容量
    private int queueCapacity = 1000;
    // 线程池中空闲线程的存活时间
    private int keepAliveSeconds = 300;
    // 线程池在关闭时最多等待的时间
    private int awaitTerminationSeconds = 60;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 线程池的最大线程数
        executor.setMaxPoolSize(maxPoolSize);
        // 线程池的核心线程数
        executor.setCorePoolSize(corePoolSize);
        // 线程池的队列容量
        executor.setQueueCapacity(queueCapacity);
        // 线程池中空闲线程的存活时间
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池在关闭时是否等待所有任务完成。
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池在关闭时最多等待的时间
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        // 线程池中线程的名称前缀
        executor.setThreadNamePrefix("AsyncThread-");
        return executor;
    }

    @Bean("doSomethingExecutor")
    public Executor doSomethingExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);
        // 最大线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(20);
        // 缓冲队列：用来缓冲执行任务的队列
        executor.setQueueCapacity(500);
        // 允许线程的空闲时间60秒：当超过了核心线程之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        executor.setThreadNamePrefix("do-something-");
        // 缓冲队列满了之后的拒绝策略：由调用线程处理（一般是主线程）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        executor.initialize();
        return executor;
    }

}
