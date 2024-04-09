package com.hxx.sbcommon.module.pipeline;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:27:40
 **/
public interface PipelineValue {

    /**
     * 节点执行
     *
     * @param pipelineContext
     * @return
     */
    boolean execute(PipelineContext pipelineContext);

}
