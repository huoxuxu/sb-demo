package com.hxx.sbConsole.safeInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationListener监听的方式是全局性的，也就是当所有的Bean都初始化完成后才会执行方法。
 * Spring 4.2 之后引入了新的 @EventListener注解
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-22 12:57:51
 **/
@Slf4j
@Component
public class EventListenerExampleBean {
    public static int counter;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("init-@EventListener");
        counter++;
    }
}
