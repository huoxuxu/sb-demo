package com.hxx.sbcommon.module.pipeline;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:30:08
 **/
public abstract class AbstractPipelineValue implements PipelineValue{

    @Override
    public boolean execute(PipelineContext pipelineContext) {


        System.out.println(this.getClass().getSimpleName() + " start ");


        boolean result = doExec(pipelineContext);


        System.out.println(this.getClass().getSimpleName() + " end ");


        return result;
    }


    protected abstract boolean doExec(PipelineContext pipelineContext);

}
