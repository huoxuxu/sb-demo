package com.hxx.sbrest.service;

import java.util.concurrent.Future;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-09 17:56:17
 **/
public interface AsyncTestService {
    /**
     * 异步调用，无返回值
     */
    void asyncTask();

    /**
     * 异步调用，有返回值
     */
    Future<String> asyncTask(String s);

    /**
     * 异步调用，无返回值，事务测试
     */
    void asyncTaskForTransaction(Boolean exFlag);

}
