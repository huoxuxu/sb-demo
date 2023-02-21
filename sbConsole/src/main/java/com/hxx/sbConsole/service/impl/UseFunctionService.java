package com.hxx.sbConsole.service.impl;

import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 9:14:56
 **/
public class UseFunctionService {
    FunctionService functionService;
    public void setFunctionService(FunctionService functionService) {
        this.functionService = functionService;
    }
    public String sayHello(String word){
        return functionService.sayHello(word);
    }

}
