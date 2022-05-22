package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 声明一个Bean的时候，可以同时指定一个initMethod属性，该属性会指向Bean的一个方法，表示在初始化后执行
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:52:25
 **/
@Slf4j
@Component
public class InitMethodExampleBean {
    @Autowired
    private Environment environment;

    @Bean(initMethod="init")
    public InitMethodExampleBean exBean() {
        return new InitMethodExampleBean();
    }

    private void init() {
        String[] defaultProfiles = environment.getDefaultProfiles();
        log.info(String.join("\n",defaultProfiles));
    }
}
