package com.hxx.sbrest.controller.cfg;

import com.hxx.sbrest.common.properties.MyAppProperties;
import com.hxx.sbrest.service.ValueAttrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("proptest")
public class PropertiesTestController {
    // 访问 my.properties 里的配置
    @Autowired
    private ValueAttrService valueAttrService;

    // 访问 application.properties 里的配置
    @Autowired
    private MyAppProperties myAppProperties;

    @RequestMapping("/t")
    public String t() {
        String str = valueAttrService.GetValueProp();
        return str;
    }

    @RequestMapping("/t2")
    public String t2() {
        int length = myAppProperties.getCode().getSms().getLength();
        return "index" + length;
    }

    /**
     * 测试读取 VM options 的配置
     *
     * @return
     */
    @RequestMapping("/t3")
    public String t3() {
        Properties properties = System.getProperties();
        String env = properties.getProperty("env");

        return "env=" + env;
    }

}
