package com.hxx.sbConsole.other.dutyChain;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:22:52
 **/
@Data
public class ProcessContext {
    /**
     * 请求
     */
    private Object request;

    /**
     * 响应
     */
    private Object response;

    /**
     * 服务开始时间
     */
    private long startServiceTime = System.currentTimeMillis();

    /**
     * 服务结束时间
     */
    private long endServiceTime;

//    /**
//     * 请求原文
//     */
//    private String jsonRequest;
//
//    /**
//     * 应答原文
//     */
//    private String jsonResponse;


    public ProcessContext(Object request) {
        this.request = request;
    }
}
