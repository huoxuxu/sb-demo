package com.hxx.sbcommon.module.event.demo;

import com.hxx.sbcommon.module.event.Event;
import com.hxx.sbcommon.module.event.EventSource;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:40:18
 **/
public class EventSourceForTest2 implements EventSource {
    @Override
    public Event fireEvent() {


        Event event = new EventForTest2();
        System.out.println(getClass().getSimpleName() + " \t fireEvent " + event.getName());


        return event;
    }
}
