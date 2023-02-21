package com.hxx.sbrest.service.impl;

import com.hxx.sbrest.service.AsyncTestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Future;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-09 17:56:51
 **/
@Service
public class AsyncTestServiceImpl implements AsyncTestService {
    @Async
    @Override
    public void asyncTask() {
        long startTime = System.currentTimeMillis();
        try {
            //模拟耗时
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：void asyncTask()，耗时：" + (endTime - startTime));
    }

    @Async("asyncTaskExecutor")
    @Override
    public Future<String> asyncTask(String s) {
        long startTime = System.currentTimeMillis();
        try {
            //模拟耗时
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：Future<String> asyncTask(String s)，耗时：" + (endTime - startTime));
        return AsyncResult.forValue(s);
    }

    @Async("asyncTaskExecutor")
    @Transactional
    @Override
    public void asyncTaskForTransaction(Boolean exFlag) {
        // 新增一个用户
        System.out.println("新增一个用户");

        if(exFlag){
            //模拟异常
            throw new RuntimeException("模拟异常");
        }
    }
}
