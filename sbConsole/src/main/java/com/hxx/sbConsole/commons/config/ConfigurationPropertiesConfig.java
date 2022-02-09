package com.hxx.sbConsole.commons.config;

import com.hxx.sbConsole.model.Dog;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 *
 * @ConfigurationProperties：告诉 SpringBoot 将本类中的所有属性和配置文件中相关的配置进行绑定；
 * prefix = "person"：配置文件中哪个下面的所有属性进行一一映射
 * <p>
 * 只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能；
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 15:53:22
 **/
@Data
@Component
// 将这个 JavaBean 中的所有属性与配置文件中以“person”为前缀的配置进行绑定
@ConfigurationProperties(prefix = "person")
public class ConfigurationPropertiesConfig {
    private String lastName;
    private Integer age;
    private Boolean boss;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birth;
    private Map<String, Object> maps;
    private List<Object> lists;
    private Dog dog;

}
