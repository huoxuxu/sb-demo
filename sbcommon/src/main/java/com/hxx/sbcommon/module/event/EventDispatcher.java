package com.hxx.sbcommon.module.event;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:42:42
 **/
public class EventDispatcher {
    /**
     * 单例模式
     */
    private static final EventDispatcher eventDispatcher = new EventDispatcher();


    private EventDispatcher() {
    }

    public EventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }


    /**
     * 分发事件
     *
     * @param event
     */
    public static void dispatchEvent(Event event) {
        if (CollectionUtils.isEmpty(EventListenerManager.getEventListenerList())) {
            return;
        }
        List<EventListener> listeners = EventListenerManager.getEventListenerList();
        for (EventListener eventListener : listeners) {
            if (eventListener.supportEvent(event)) {
                eventListener.handlerEvent(event);
            }
        }

    }
}
