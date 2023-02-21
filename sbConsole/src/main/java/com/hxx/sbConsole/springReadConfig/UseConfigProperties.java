package com.hxx.sbConsole.springReadConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-23 16:28:16
 **/
@ConfigurationProperties(prefix = "cust")
public class UseConfigProperties {
    @Value("${spring.profiles.active}")
    private String active;

    @Value("${apple}")
    private String apple;
    @Value("${orange}")
    private String orange;

    public void show() {
        System.out.println("active --- > " + active);
        System.out.println("apple --- > " + apple);
        System.out.println("orange --- > " + orange);
    }
//    @Bean
//    public DataSource druidDataSource() {
//        LOG.info("主引教程----项目【"+myName+"】初始化数据库------"+environment.getProperty("my.name"));
//        LOG.info("主引教程----项目【"+environment.getProperty("my.name")+"】初始化数据库------");
//        singleConfig.show();
//
//        return new DruidDataSource();
//    }

}
