package com.hxx.sbcommon.module.event.demo;

import com.hxx.sbcommon.module.event.Event;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:41:22
 **/
public class EventForTest2 implements Event {
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
