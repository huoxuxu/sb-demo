package com.hxx.sbConsole.springmodule.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Slf4j
@Service
public class AsyncTaskDemoService {

    @Async
    public void execNoReturnTask() {
        log.info("无返回值的异步调用开始" + Thread.currentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("无返回值的异步调用结束" + Thread.currentThread().getName());
    }

    @Async
    public Future<String> execHaveReturnTask() {
        Future<String> future;
        try {
            Thread.sleep(1000);
            future = new AsyncResult<>("success:");
        } catch (InterruptedException e) {
            future = new AsyncResult<>("error");
        }
        return future;
    }


}
