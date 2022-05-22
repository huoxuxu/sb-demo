package com.hxx.sbConsole.safeInit.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 初始化的顺序为：
 * 构造器方法
 * PostConstruct 注解方法
 * InitializingBean的afterPropertiesSet()
 * Bean定义的initMethod属性方法
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 13:04:29
 **/
@Slf4j
@Component
@Scope(value = "prototype")
public class AllStrategiesExampleBean implements InitializingBean {

    public AllStrategiesExampleBean() {
        log.info("Constructor");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("InitializingBean");
    }

    @PostConstruct
    public void postConstruct() {
        log.info("PostConstruct");
    }

    //在XML中定义为initMethod
    public void init() {
        log.info("init-method");
    }
}
