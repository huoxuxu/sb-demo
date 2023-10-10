package com.hxx.sbConsole.safeInit.appListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:51:29
 **/
@Slf4j
@Component
public class ApplicationListenerExample3 implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        log.info("init-ApplicationListener<ApplicationEnvironmentPreparedEvent>3");

    }
}
