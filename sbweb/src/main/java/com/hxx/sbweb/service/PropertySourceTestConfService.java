package com.hxx.sbweb.service;

import com.hxx.sbservice.model.attr.PropertySource.PropertySourceTestConf;
import com.hxx.sbservice.model.attr.PropertySource.PropertySourceXmlTestConf;
import com.hxx.sbservice.model.attr.PropertySource.PropertySourceYmlTestConf;
import com.hxx.sbweb.common.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 9:28:18
 **/
@Service
public class PropertySourceTestConfService {
    @Autowired
    private PropertySourceTestConf conf;
    @Autowired
    private PropertySourceXmlTestConf xmlConf;
    @Autowired
    private PropertySourceYmlTestConf ymlConf;

    public void testPropertySource() {
        String json = JsonUtil.toJSON(conf);
        String json2 = JsonUtil.toJSON(xmlConf);
        String json3 = JsonUtil.toJSON(ymlConf);
        System.out.println(json + "\n" + json2 + "\n" + json3);
    }
}
