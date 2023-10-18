package com.hxx.sbConsole.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 13:06:25
 **/
@Configuration
@ComponentScan("com.hxx")
public class ElConfig {
    @Value("haha")
    private String normal;
    @Value("#{systemProperties['os.name']}")
    private String osName;
    @Value("#{ T(java.lang.Math).random()*100.0}")
    private double number;
    // #{} 表示SpEl表达式通常用来获取bean的属性，或者调用bean的某个方法。当然还有可以表示常量
//    @Value("#{demo.anothor}")
//    private String fromAnthor;
    @Value("classpath:test.txt")
    private Resource testFile;
    @Value("http://www.baidu.com")
    private Resource testUrl;
    // 从配置properties文件中读取 book.name 的值
    @Value("${book.name}")
    private String bookname;

    @Autowired
    private Environment environment;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource() throws IOException {
        System.out.println(normal);
        System.out.println(osName);
        System.out.println(number);
//        System.out.println(fromAnthor);
        //System.out.println(IOUtils.toString(testFile.getInputStream()));
        System.out.println(bookname);
        System.out.println(environment.getProperty("book.author"));
        //System.out.println(IOUtils.toString(testUrl.getInputStream()));
    }


}
