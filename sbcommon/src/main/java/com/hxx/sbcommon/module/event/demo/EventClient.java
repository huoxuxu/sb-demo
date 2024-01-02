package com.hxx.sbcommon.module.event.demo;

import com.hxx.sbcommon.module.event.EventDispatcher;
import com.hxx.sbcommon.module.event.EventListener;
import com.hxx.sbcommon.module.event.EventListenerManager;
import com.hxx.sbcommon.module.event.EventSource;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:43:37
 **/
public class EventClient {
    public static void main(String[] args) {

        // 创建事件监听器
        EventListener eventListener = new EventListenerForTest();
        EventListenerManager.addEventListener(eventListener);

        // 创建事件源
        EventSource eventSourceForTest = new EventSourceForTest();
        EventSource eventSourceForTest2 = new EventSourceForTest2();

        // 发布事件
        EventDispatcher.dispatchEvent(eventSourceForTest.fireEvent());
        EventDispatcher.dispatchEvent(eventSourceForTest2.fireEvent());


    }

}
