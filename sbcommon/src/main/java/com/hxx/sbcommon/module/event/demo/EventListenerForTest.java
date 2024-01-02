package com.hxx.sbcommon.module.event.demo;

import com.hxx.sbcommon.module.event.Event;
import com.hxx.sbcommon.module.event.EventListener;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:42:12
 **/
public class EventListenerForTest implements EventListener {
    @Override
    public boolean supportEvent(Event event) {
        return event.getName().contains("Test");
    }


    @Override
    public boolean handlerEvent(Event event) {
        System.out.println(this.getClass().getSimpleName() + "\t handler " + event.getName());

        return true;
    }
}
