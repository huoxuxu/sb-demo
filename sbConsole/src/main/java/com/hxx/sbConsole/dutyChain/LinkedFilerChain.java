package com.hxx.sbConsole.dutyChain;

import com.hxx.sbConsole.dutyChain.base.IFilterCallback;
import com.hxx.sbConsole.dutyChain.base.IFilterChain;
import com.hxx.sbConsole.dutyChain.base.IProcessFilter;

import java.util.LinkedList;

/**
 * 基于LinkedList的双向责任链
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:26:01
 **/
public class LinkedFilerChain implements IFilterChain {

    private LinkedList<IProcessFilter> filters;

    public LinkedFilerChain(LinkedList<IProcessFilter> filters) {
        this.filters = filters;
    }

    /**
     * 执行责任链
     * @param processContext
     * @param callback
     * @throws Exception
     */
    @Override
    public void process(ProcessContext processContext, IFilterCallback callback) throws Exception {
        assert this.filters != null && !this.filters.isEmpty();

        // 从第一个filter开始执行
        IProcessFilter processFilter = this.filters.removeFirst();
        processFilter.process(processContext, this, callback);
    }
}