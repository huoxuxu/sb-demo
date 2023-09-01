package com.hxx.sbMyBatis;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@EnableConfigurationProperties
@ConfigurationPropertiesScan
// 扫描Mybatis mapper包
@MapperScan("com.hxx.sbMyBatis.dal.mapper")
@SpringBootApplication
public class SbMyBatisApplication {
    public static void main(String[] args) {
        // 标准
        SpringApplication.run(SbMyBatisApplication.class, args);

    }
}
