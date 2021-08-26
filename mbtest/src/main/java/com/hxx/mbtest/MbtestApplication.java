package com.hxx.mbtest;

import com.hxx.sbcommon.common.LocalDateTimeUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.util.Date;

@MapperScan("com.hxx.mbtest.mapper")
@ConfigurationPropertiesScan

// 启用定时调度
@EnableScheduling
@EnableAsync

@SpringBootApplication
public class MbtestApplication {

    public static void main(String[] args) {
        // SignUtils.main(args);

        Integer num5 = 128;
        int num6 = 128;
        System.out.println("num5==num6 " + (num5 == num6));

        {

            Date date = new Date(1628017862599L);
            LocalDateTime lt= LocalDateTimeUtil.parse(date);
            System.out.println(lt);
        }
        SpringApplication.run(MbtestApplication.class, args);
    }

}
