package com.hxx.sbcommon.module.event;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:41:44
 **/
public interface EventListener {

    /**
     * 是否支持此事件
     *
     * @param event
     * @return
     */
    boolean supportEvent(Event event);


    /**
     * 处理事件
     *
     * @return
     */
    boolean handlerEvent(Event event);

}
