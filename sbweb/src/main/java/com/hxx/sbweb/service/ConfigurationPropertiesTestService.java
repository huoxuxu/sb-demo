package com.hxx.sbweb.service;

import com.hxx.sbservice.model.attr.ConfigurationProperties.ConfigurationPropertiesTestConf;
import com.hxx.sbweb.common.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 13:42:13
 **/
@Service
public class ConfigurationPropertiesTestService {

    @Autowired
    private ConfigurationPropertiesTestConf conf;


    public void testConfigurationProperties() {
        String json = JsonUtil.toJSON(conf);
        System.out.println(json);
    }

}
