package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 如果依赖的字段在Bean的构造方法中声明，那么Spring框架会先实例这些字段对应的Bean，再调用当前的构造方法。
 * 此时，构造方法中的一些操作也是安全的
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:55:00
 **/
@Slf4j
@Component
public class LogicInConstructorExampleBean {

    private final Environment environment;

    @Autowired
    public LogicInConstructorExampleBean(Environment environment) {
        //environment实例已初始化
        this.environment = environment;

        String[] defaultProfiles = this.environment.getDefaultProfiles();
        log.info(String.join("\n",defaultProfiles));
    }
}
