package com.hxx.sbConsole.other.dutyChain.base;

import com.hxx.sbConsole.other.dutyChain.ProcessContext;

/**
 * 责任链调用接口
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:22:07
 **/
public interface IProcessFilter {
    /**
     * 链节点的处理
     * 1、当前节点需要持有链表FilterChain以继续调用后续链的执行
     * 2、同时需要持有上个节点传递过来的回调对象，以执行完后回调到上个节点的后处理流程。
     * @param processContext
     * @param filterChain
     * @param callback
     * @throws Exception
     */
    void process(ProcessContext processContext, IFilterChain filterChain, IFilterCallback callback)
            throws Exception;

}
