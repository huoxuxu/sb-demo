package com.hxx.sbweb.controller;

import com.hxx.sbcommon.model.Result;
import com.hxx.sbservice.model.JdbcModel;
import com.hxx.sbservice.model.attr.Value.ValueTestConf;
import com.hxx.sbweb.common.JsonUtil;
import com.hxx.sbweb.service.ConfigurationPropertiesTestService;
import com.hxx.sbweb.service.PropertySourceTestConfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * mybatis 注解方式示例
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Autowired
    private PropertySourceTestConfService confSer;
    @Autowired
    private ConfigurationPropertiesTestService confPropSer;

    @Autowired
    private ValueTestConf valConf;

    @Resource(name = "newJdbcModel")
    private JdbcModel jdbcModel;


    /**
     * http://localhost:8082/attr/get?name=123
     *
     * @param name
     * @return
     */
    @RequestMapping("/get")
    public Result<Integer> get(@RequestParam(value = "name") String name) {
        confSer.testPropertySource();
        confPropSer.testConfigurationProperties();

        String jdbcModelJson = JsonUtil.toJSON(jdbcModel);

        String valConfJson = JsonUtil.toJSON(valConf);

        return Result.success(1);
    }


}
