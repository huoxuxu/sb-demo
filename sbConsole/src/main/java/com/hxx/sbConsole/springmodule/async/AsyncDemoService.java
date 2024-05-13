package com.hxx.sbConsole.springmodule.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-04-25 21:09:34
 **/
@Slf4j
@Service
public class AsyncDemoService {

    // 指定使用beanname为doSomethingExecutor的线程池

    /**
     *
     * @param message
     * @return
     */
    @Async("doSomethingExecutor")
    public String doSomething(String message) {
        log.info("do something, message={}", message);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("do something error: ", e);
        }
        return message;
    }

    @Async("doSomethingExecutor")
    public CompletableFuture<String> doSomething1(String message) throws InterruptedException {
        log.info("do something1: {}", message);
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("do something1: " + message);
    }

}
