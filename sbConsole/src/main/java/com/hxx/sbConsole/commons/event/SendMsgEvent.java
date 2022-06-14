package com.hxx.sbConsole.commons.event;

import org.springframework.context.ApplicationEvent;

/*
Spring的事件为Bean与Bean之间的通信提供了支持，当我们系统中某个Spring管理的Bean处理完某件事后，希望让其他Bean收到通知并作出相应的处理，这时可以让其他Bean监听当前这个Bean所发送的事件。

要实现事件的监听，我们要做两件事：
1：自定义事件，继承ApplicationEvent接口
2：定义事件监听器，实现ApplicationListener
3：事件发布类
*/
/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-06-13 16:08:54
 **/
//@Component
public class SendMsgEvent extends ApplicationEvent {

    private static final long serialVersionID = 1L;

    // 收件人
    public String receiver;

    // 收件内容
    public String content;

    public SendMsgEvent(Object source) {
        super(source);
    }

    public SendMsgEvent(Object source, String receiver, String content) {
        super(source);
        this.receiver = receiver;
        this.content = content;
    }

    public void output(){
        System.out.println("I had been sand a msg to " + this.receiver);
    }
}