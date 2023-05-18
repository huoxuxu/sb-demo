package com.hxx.sbrest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;

@SpringBootApplication
@MapperScan("com.hxx.sbrest.mapper")
@ConfigurationPropertiesScan

// 启用定时调度
@EnableScheduling
@EnableAsync//开启异步调用
public class SbrestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbrestApplication.class, args);
    }

}
