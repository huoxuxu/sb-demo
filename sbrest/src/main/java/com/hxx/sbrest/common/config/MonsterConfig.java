package com.hxx.sbrest.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-06-09 10:42:34
 **/
@Component
@PropertySource(value = {
        "classpath:my.properties",
}, encoding = "utf-8")
@ConfigurationProperties(prefix = "monster")
@Data
public class MonsterConfig {
    private String name;
    private int atk;

}
