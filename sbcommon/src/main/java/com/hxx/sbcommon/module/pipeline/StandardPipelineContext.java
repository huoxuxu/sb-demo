package com.hxx.sbcommon.module.pipeline;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:29:19
 **/
public class StandardPipelineContext implements PipelineContext{
    private Map<String, Object> contentMap = Maps.newConcurrentMap();


    @Override
    public void set(String contextKey, Object contextValue) {
        contentMap.put(contextKey, contextValue);
    }


    @Override
    public Object get(String contextKey) {
        return contentMap.get(contextKey);
    }
}
