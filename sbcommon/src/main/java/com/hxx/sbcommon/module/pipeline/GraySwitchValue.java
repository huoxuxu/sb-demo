package com.hxx.sbcommon.module.pipeline;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:30:37
 **/
public class GraySwitchValue extends AbstractPipelineValue{

    @Override
    public boolean doExec(PipelineContext pipelineContext) {


        pipelineContext.set(PipelineContext.FOR_TEST, true);


        return true;
    }
}
