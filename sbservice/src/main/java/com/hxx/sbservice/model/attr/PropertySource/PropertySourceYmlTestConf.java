package com.hxx.sbservice.model.attr.PropertySource;

import com.hxx.sbservice.commons.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Repository;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 9:24:15
 **/
@Data
@PropertySource(value = "classpath:jdbcyml.yml",factory = YamlPropertySourceFactory.class)
@Repository
public class PropertySourceYmlTestConf {

    @Value("${jdbcy.driver}")
    private String driver;
    @Value("${jdbcy.url}")
    private String url;
    @Value("${jdbcy.userName}")
    private String userName;
    @Value("${jdbcy.password}")
    private String password;


}
