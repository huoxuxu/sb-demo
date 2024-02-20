package com.hxx.sbConsole.other.dynamicProxy.code;

import com.hxx.sbConsole.service.ICodeProxySubject;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-18 18:36:04
 **/
public class CodeProxySubject implements ICodeProxySubject {
    private ICodeProxySubject ins;

    public CodeProxySubject(ICodeProxySubject ins) {
        this.ins = ins;
    }

    @Override
    public void doSomething(String thing) {
        this.ins.doSomething(thing);
    }

}
