package com.hxx.sbweb.controller;

import com.hxx.sbcommon.model.Result;
import com.hxx.sbservice.model.JdbcModel;
import com.hxx.sbservice.model.attr.Value.ValueTestConf;
import com.hxx.sbweb.common.JsonUtil;
import com.hxx.sbweb.service.ConfigurationPropertiesTestService;
import com.hxx.sbweb.service.PropertySourceTestConfService;
import com.hxx.sbweb.service.impl.DemoMyBatisServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * mybatis 注解方式示例
 */
@RestController
@RequestMapping("/demo")
public class DemoTestController {
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


}
