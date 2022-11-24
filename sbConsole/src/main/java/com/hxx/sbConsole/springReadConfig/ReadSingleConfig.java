package com.hxx.sbConsole.springReadConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 读取单独一个配置文件
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-23 16:00:23
 **/
@Configuration
@PropertySource("classpath:application.yml")
public class ReadSingleConfig {
    @Value("${spring.profiles.active}")
    private String active;

    @Value("${cust.apple}")
    private String apple;
    @Value("${cust.orange}")
    private String orange;

    public void show() {
        System.out.println("active --- > " + active);
        System.out.println("apple --- > " + apple);
        System.out.println("orange --- > " + orange);
    }

}
