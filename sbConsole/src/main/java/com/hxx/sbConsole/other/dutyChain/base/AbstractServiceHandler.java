package com.hxx.sbConsole.other.dutyChain.base;

import com.hxx.sbConsole.other.dutyChain.ProcessContext;

/**
 * 抽象serviceHandler基类
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:27:12
 **/
public abstract class AbstractServiceHandler implements IProcessFilter {
    @Override
    public void process(ProcessContext processContext, IFilterChain filterChain, IFilterCallback callback)
            throws Exception {
        /**
         * 注意这里handle处理因为是责任链最后一个节点，只需执行回调即可，无需持有filterChain
         */
        handle(processContext, callback);
    }

    /**
     *
     * @param processContext
     * @param callback
     * @throws Exception
     */
    abstract public void handle(ProcessContext processContext, IFilterCallback callback) throws Exception;
}