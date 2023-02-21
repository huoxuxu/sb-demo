package com.hxx.sbConsole.commons.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-13 16:09:45
 **/
@Component
public class MsgListener implements ApplicationListener<SendMsgEvent> {
    @Override
    public void onApplicationEvent(SendMsgEvent sendMsgEvent) {
        sendMsgEvent.output();
        System.out.println(sendMsgEvent.receiver + "received msg : " + sendMsgEvent.content );
    }
}