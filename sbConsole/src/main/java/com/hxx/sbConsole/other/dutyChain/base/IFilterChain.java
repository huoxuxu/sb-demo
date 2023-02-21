package com.hxx.sbConsole.other.dutyChain.base;

import com.hxx.sbConsole.other.dutyChain.ProcessContext;

/**
 * 责任链
 * 只需持有处理上下文、回调对象即可
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:24:48
 **/
public interface IFilterChain {

    /**
     * @param processContext
     * @param callback
     * @throws Exception
     */
    void process(ProcessContext processContext, IFilterCallback callback) throws Exception;

}
