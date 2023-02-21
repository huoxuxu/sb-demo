package com.hxx.sbConsole.commons.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-13 16:10:55
 **/
@Component
public class Publisher {
    @Autowired
    ApplicationContext applicationContext;

    public void publish(Object source, String receiver, String content){
        applicationContext.publishEvent(new SendMsgEvent(source, receiver, content));
    }
}
