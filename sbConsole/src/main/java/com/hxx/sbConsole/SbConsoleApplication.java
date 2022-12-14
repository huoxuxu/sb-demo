package com.hxx.sbConsole;

//import com.hxx.sbConsole.commons.config.javaConfig;

import com.hxx.sbConsole.commons.config.ElConfig;
import com.hxx.sbConsole.commons.config.MyWebConfig;
import com.hxx.sbConsole.commons.event.Publisher;
import com.hxx.sbConsole.service.UserService;
import com.hxx.sbConsole.service.impl.DemoAnnotationService;
import com.hxx.sbConsole.service.impl.DemoMethodService;
import com.hxx.sbConsole.service.impl.UseFunctionService;
import com.hxx.sbcommon.common.hardware.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@Slf4j
@EnableAsync
@EnableConfigurationProperties
@EnableScheduling
@ConfigurationPropertiesScan
// 排除数据库配置扫描
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SbConsoleApplication {

    public static void main(String[] args) {
        {
            String localIP = NetUtil.getLocalIP();
            String localMac = NetUtil.getLocalMac();
            System.out.println("本机信息：" + localIP + " " + localMac);
        }
        // 标准
        SpringApplication.run(SbConsoleApplication.class, args);

    }

    // 测试 AnnotationConfigApplicationContext 功能
//    static void case0() {
//        // 基于Java的配置类加载Spring的应用上下文
//        // 避免使用application.xml进行配置。相比XML配置，更加便捷
//        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(javaConfig.class)) {
//            UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
//            String ioc = useFunctionService.sayHello("ioc");
//            System.out.println(ioc);
//        }
//    }

    // 测试 AnnotationConfigApplicationContext 的AOP
//    static void case1() {
//        try (AnnotationConfigApplicationContext context =
//              new AnnotationConfigApplicationContext(com.hxx.sbConsole.commons.config.AopConfig.class)) {
//            UserService u1 = context.getBean(UserService.class);
//            u1.selectById(1);
//            DemoAnnotationService s1 = context.getBean(DemoAnnotationService.class);
//            s1.add();
//            DemoMethodService s2 = context.getBean(DemoMethodService.class);
//            s2.add();
//        }
//    }

    // 测试SpringEL
//    static void case2() {
//        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ElConfig.class)) {
//            ElConfig s1 = context.getBean(ElConfig.class);
//            s1.outputResource();
//        } catch (Exception e) {
//            log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
//        }
//    }

    // WebConfig.class主要是为了扫描Publisher 和Listener类。里面有两个注解@ComponenScan和@Configuration。
//    static void main(String[] args) {
//        AnnotationConfigApplicationContext applicationContext =
//                new AnnotationConfigApplicationContext(MyWebConfig.class);
//        Publisher publisher = applicationContext.getBean(Publisher.class);
//        publisher.publish("Hello,World!","Mr.Lensen", "I Love U");
//    }

}
