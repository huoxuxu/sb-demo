package com.hxx.sbweb;

import com.hxx.sbweb.common.SignUtils;
import com.hxx.sbweb.model.CustomerInfoModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("com.hxx.sbweb.mapper")
//@ComponentScan(value = "com.hxx")
// 多包扫描
@ComponentScans(value =
        {
//        @ComponentScan(value = "com.hxx.sbweb"),//可以省略当前包扫描
                @ComponentScan(value = "com.hxx.sbservice")
        })
public class SbwebApplication {

    public static void main(String[] args) {
        // SignUtils.main(args);

        SpringApplication.run(SbwebApplication.class, args);
    }

}
