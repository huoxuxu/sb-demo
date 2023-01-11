package com.hxx.sbConsole.other.dutyChain.base;

import com.hxx.sbConsole.other.dutyChain.ProcessContext;

/**
 * 责任链回调接口
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 11:23:47
 **/
public interface IFilterCallback {
    /**
     * 回调，因为是下个节点直接调用，除上下文外无需多余参数
     * @param context
     * @throws Exception
     */
    void onPostProcess(ProcessContext context) throws Exception;

}
