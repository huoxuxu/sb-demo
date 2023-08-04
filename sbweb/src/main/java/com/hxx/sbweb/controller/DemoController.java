package com.hxx.sbweb.controller;

import com.hxx.sbcommon.model.Result;
import com.hxx.sbweb.controller.base.BaseRestController;
import com.hxx.sbweb.model.StuModel;
import com.hxx.sbweb.service.impl.DemoMyBatisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * mybatis 注解方式示例
 */
@RestController
@RequestMapping("/demo")
public class DemoController extends BaseRestController {
    @Autowired
    private DemoMyBatisServiceImpl demoMyBatisService;


    /**
     * http://localhost:8082/demo/case1
     *
     * @return
     */
    @RequestMapping("/case1")
    public Result<String> case1() {
        String ret = demoMyBatisService.demo();

        return Result.success(ret);
    }

    @RequestMapping("/case2")
    public Result<String> case2(@RequestBody StuModel model) {
        String ret = demoMyBatisService.demo();

        return Result.success(ret);
    }

}
