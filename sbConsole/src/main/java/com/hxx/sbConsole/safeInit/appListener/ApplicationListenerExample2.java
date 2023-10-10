package com.hxx.sbConsole.safeInit.appListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationEnvironmentPreparedEvent：Spring Framework的一个事件类，它在Spring应用程序的上下文环境准备就绪后被触发。
 * 这个事件提供了应用程序环境准备完成后的上下文信息，包括应用程序的配置路径，环境属性等。
 * 注意：需要在META-INF/spring.factories里添加org.springframework.context.ApplicationListener
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:51:29
 **/
@Slf4j
@Component
public class ApplicationListenerExample2 implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log.info("init-ApplicationListener<ApplicationEnvironmentPreparedEvent>2");

    }
}
