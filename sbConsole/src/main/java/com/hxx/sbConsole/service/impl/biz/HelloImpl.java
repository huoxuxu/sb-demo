package com.hxx.sbConsole.service.impl.biz;

import com.hxx.sbConsole.service.Hello;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 15:06:35
 **/
public class HelloImpl implements Hello {
    @Override
    public void morning(String name) {
        System.out.println("你好：" + name);
    }

    @Override
    public void world(String name) {
        System.out.println("世界你好：" + name);
    }


}
