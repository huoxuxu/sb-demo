package com.hxx.sbservice.model.attr.Async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 14:41:55
 **/
@Service
@Async
public class SyncService {

    //@Async
    public Future<String> method1(String str) throws InterruptedException {
        Thread.sleep(1000 * 2);
        return new AsyncResult<>(str);
    }

    //@Async
    public Future<String> method2(String str) throws InterruptedException {
        Thread.sleep(1000 * 1);
        return new AsyncResult<>(str);
    }

    // @Async
    public Future<String> method3(String str) throws InterruptedException {
        Thread.sleep(1000 * 3);
        return new AsyncResult<>(str);
    }

}
