package com.hxx.sbcommon.module.event;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:43:09
 **/
public class EventListenerManager {
    private static List<EventListener> eventListenerList = Lists.newArrayList();


    /**
     * 添加事件监听器
     *
     * @param eventListener
     * @return
     */
    public static boolean addEventListener(EventListener eventListener) {
        if (!eventListenerList.contains(eventListener)) {
            return eventListenerList.add(eventListener);
        }


        return true;
    }


    /**
     * 移除事件监听器
     *
     * @param eventListener
     * @return
     */
    public static boolean removeEventListener(EventListener eventListener) {
        if (eventListenerList.contains(eventListener)) {
            return eventListenerList.remove(eventListener);
        }


        return true;
    }


    public static List<EventListener> getEventListenerList() {
        return eventListenerList;
    }

}
