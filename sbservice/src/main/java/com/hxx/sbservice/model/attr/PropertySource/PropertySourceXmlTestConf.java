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
@PropertySource(value = "classpath:jdbcxml.xml")
@Repository
public class PropertySourceXmlTestConf {

    @Value("${jdbcx.driver}")
    private String driver;
    @Value("${jdbcx.url}")
    private String url;
    @Value("${jdbcx.userName}")
    private String userName;
    @Value("${jdbcx.password}")
    private String password;


}
