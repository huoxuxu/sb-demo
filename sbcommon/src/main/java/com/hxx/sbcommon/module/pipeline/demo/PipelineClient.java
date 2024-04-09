package com.hxx.sbcommon.module.pipeline.demo;

import com.hxx.sbcommon.module.pipeline.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:31:43
 **/
public class PipelineClient {
    public static void main(String[] args) {

        // 管道初始化
        Pipeline pipeline = new StandardPipeline();


        // value扩展
        PipelineValue pipelineValue = new GraySwitchValue();
        PipelineValue pipelineValue2 = new ForTestValue();


        // 上下文
        PipelineContext pipelineContext = new StandardPipelineContext();


        pipeline.addValue(pipelineValue);
        pipeline.addValue(pipelineValue2);


        // 调用管道
        pipeline.invoke(pipelineContext);


    }

}
