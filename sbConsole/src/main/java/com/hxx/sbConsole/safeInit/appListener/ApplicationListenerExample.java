package com.hxx.sbConsole.safeInit.appListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 与前面的InitializingBean不同的是，通过ApplicationListener监听的方式是全局性的，也就是当所有的Bean都初始化完成后才会执行方法
 * ContextRefreshedEvent: 在Spring上下文初始化完成后，这里定义的方法将会被执行。
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:56:10
 **/
@Slf4j
@Component
public class ApplicationListenerExample implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("init-ApplicationListener<ContextRefreshedEvent>");

    }
}
