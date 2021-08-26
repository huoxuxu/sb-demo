package com.hxx.sbweb;

import com.hxx.sbweb.common.SignUtils;
import com.hxx.sbweb.model.CustomerInfoModel;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@MapperScan("com.hxx.sbweb.mapper")
public class SbwebApplication {

    public static void main(String[] args) {
        // SignUtils.main(args);



        SpringApplication.run(SbwebApplication.class, args);
    }

}
