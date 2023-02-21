package com.hxx.sbConsole.service.impl;

import com.hxx.sbConsole.service.ICodeProxySubject;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-18 18:34:49
 **/
public class CodeProxySubjectImpl implements ICodeProxySubject {
    @Override
    public void doSomething(String thing) {
        System.out.println("搞起来：" + thing);
    }
}
