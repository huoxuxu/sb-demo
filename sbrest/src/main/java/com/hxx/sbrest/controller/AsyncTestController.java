package com.hxx.sbrest.controller;

import com.hxx.sbrest.controller.base.BaseController;
import com.hxx.sbrest.service.AsyncTestService;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("async")
public class AsyncTestController extends BaseController {

    @Autowired
    private AsyncTestService asyncTestService;

    @RequestMapping("/t")
    public String t() {
        long startTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：开始调用异步业务");

        //无返回值
        asyncTestService.asyncTask();

        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "：调用异步业务结束，耗时：" + (endTime - startTime));

        return ok("9");
    }

    @RequestMapping("/t1")
    public String t1() {
        long startTime = System.currentTimeMillis();
        //有返回值，但主线程不需要用到返回值
        System.out.println(Thread.currentThread().getName() + "：开始调用异步业务");
        Future<String> future = asyncTestService.asyncTask("huanzi-qch");

        try {
            String str = future.get();

            long endTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + ":"+str+" :调用异步业务结束，耗时：" + (endTime - startTime));
        } catch (Exception ex) {
            return ok(-1);
        }
        return ok("99");
    }


}
