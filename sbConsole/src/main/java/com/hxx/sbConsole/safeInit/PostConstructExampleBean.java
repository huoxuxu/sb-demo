package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 声明一个Bean对象初始化完成后执行的方法。
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:49:07
 **/
@Slf4j
@Component
public class PostConstructExampleBean {
    @Autowired
    private Environment environment;

    @PostConstruct
    public void init() {
        //environment 已经注入
        String[] defaultProfiles = environment.getDefaultProfiles();
        log.info(String.join("\n", defaultProfiles));
    }
}
