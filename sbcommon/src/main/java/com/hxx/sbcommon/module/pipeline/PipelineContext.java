package com.hxx.sbcommon.module.pipeline;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:27:32
 **/
public interface PipelineContext {

    String FOR_TEST = "forTest";


    /**
     * 设置
     *
     * @param contextKey
     * @param contextValue
     */
    void set(String contextKey, Object contextValue);


    /**
     * 获取值
     *
     * @param contextKey
     * @return
     */
    Object get(String contextKey);
}
