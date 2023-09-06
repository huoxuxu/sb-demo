package com.hxx.sbConsole.commons.config;

import com.hxx.sbConsole.model.inherit.Dog;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 16:34:05
 **/
@Data
@PropertySource(value = "classpath:person.properties")//指向对应的配置文件
@Component
@ConfigurationProperties(prefix = "person1")// 如果prefix的值，在application.yml中，则会报错
public class PropertySourceConfig {
    private String lastName;
    private Integer age;
    private Boolean boss;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birth;
    private Map<String, Object> maps;
    private List<Object> lists;
    private Dog dog;

}
