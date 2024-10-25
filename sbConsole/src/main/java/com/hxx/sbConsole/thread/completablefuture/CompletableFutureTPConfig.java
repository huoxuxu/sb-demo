package com.hxx.sbConsole.thread.completablefuture;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池
 * 使用：
 * <p>
 * Autowired
 * Qualifier("taskExecutor")
 * private Executor taskExecutor;
 */
@Configuration
@EnableAsync
public class CompletableFutureTPConfig {

    /*
    线程池拒绝策略
        AbortPolicy（默认）
         直接抛出RejectedExecutionException异常，阻止系统继续处理新任务。
        CallerRunsPolicy
         在调用者线程中执行被拒绝的任务。这样做会使任务的提交速度变慢，因为调用者线程要花时间执行任务，而不是仅仅提交任务后就返回。
        DiscardPolicy
         直接丢弃新提交的任务，不做任何处理，也不抛出异常
        DiscardOldestPolicy
         丢弃任务队列中最旧的任务，然后将新任务添加到队列中
    */

    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数，即线程池中始终保持存活的线程数量，除非设置 allowCoreThreadTimeOut为true
        threadPoolTaskExecutor.setCorePoolSize(16);
        // 最大线程数，当任务队列已满且核心线程都在忙碌时，如果有新任务提交，则创建新线程，直到达到最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(100);
        // 当线程数和大于核心线程数时，多余的空闲线程在等待任务时的最长存活时间为60s，超出此时间则终止这些空闲线程
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        // 任务队列的容量为200.当线程池中的线程都在忙碌时，新提交的任务会被放入这个队列等待执行
        threadPoolTaskExecutor.setQueueCapacity(200);
        // 当线程池和任务队列都已满时，使用的拒绝策略
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 为线程池中的线程设置名称前缀
        threadPoolTaskExecutor.setThreadNamePrefix("asyncTaskExecutor-");

        // 当应用关闭时，线程池会等待正在执行的任务完成后再关闭
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 在调用 shutdown 方法后，最多等待60s让线程池中的任务完成并关闭线程池，超过则强制终止
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);

        return threadPoolTaskExecutor;
    }


}
