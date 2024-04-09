package com.hxx.sbcommon.module.event;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:39:20
 **/
public interface EventSource {

    /**
     * 发出事件
     *
     * @return
     */
    Event fireEvent();

}
