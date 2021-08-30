package com.hxx.sbservice.model.attr.ConfigurationProperties;

import com.hxx.sbservice.model.JdbcModel;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 13:55:29
 **/
@Configuration
public class ConfigurationPropertiesMethodTestConf {
    @ConfigurationProperties(prefix = "jdbc2")
    @Bean(name = "newJdbcModel")
    public JdbcModel newJdbcModel() {
        JdbcModel m = new JdbcModel();
        return m;
    }


}
