package com.hxx.sbservice.model.attr.ConfigurationProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 13:40:24
 **/
@Data
@Component
@ConfigurationProperties(prefix = "jdbc")
public class ConfigurationPropertiesTestConf {

    private String driver;
    private String url;
    private String userName;
    private String password;

}
