package com.hxx.sbcommon.module.pipeline;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:27:09
 **/
public interface Pipeline {
    /**
     * 执行
     *
     * @return
     */
    boolean invoke(PipelineContext pipelineContext);


    /**
     * 添加值
     *
     * @param pipelineValue
     * @return
     */
    boolean addValue(PipelineValue pipelineValue);


    /**
     * 移除值
     *
     * @param pipelineValue
     * @return
     */
    boolean removeValue(PipelineValue pipelineValue);
}
