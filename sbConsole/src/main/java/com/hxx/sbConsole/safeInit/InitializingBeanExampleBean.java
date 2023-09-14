package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * InitializingBean 是由Spring框架提供的接口，其与@PostConstruct注解的工作原理非常类似
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:51:24
 **/
@Slf4j
@Component
public class InitializingBeanExampleBean implements InitializingBean {
    @Autowired
    private Environment environment;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("init-InitializingBean");
        //environment 已经注入
        String[] defaultProfiles = environment.getDefaultProfiles();
        log.info(String.join("\n", defaultProfiles));
    }
}