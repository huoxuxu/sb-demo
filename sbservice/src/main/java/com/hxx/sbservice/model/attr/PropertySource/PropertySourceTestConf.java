package com.hxx.sbservice.model.attr.PropertySource;

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
@PropertySource(value = {"classpath:jdbc.properties", "classpath:myconf.properties"})
@Repository
public class PropertySourceTestConf {

    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.userName}")
    private String userName;
    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbcm.userName}")
    private String userName2;

}
