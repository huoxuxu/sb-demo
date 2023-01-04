package com.hxx.sbConsole.commons.config;

import com.hxx.sbConsole.commons.annotation.ConditionalOnFrequency;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-03 16:36:26
 **/
@Configuration
// 如果条件匹配，则会处理这个类
@ConditionalOnFrequency(value = "frequency", havingValue = "true")
public class FrequencyAutoConfiguration {
    @PostConstruct
    public void observer(){
        System.out.println("loading");
    }
}
