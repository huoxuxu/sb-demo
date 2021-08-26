package com.hxx.sbrest.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-09 10:25:23
 **/
@Component
@PropertySource(value = {
        "classpath:my.properties",
}, encoding = "utf-8")
@Data
public class MyConfig {
    @Value("${name}")
    private String lever;
    @Value("${atk}")
    private int atk;
    @Value("${desc}")
    private String desc;

}
