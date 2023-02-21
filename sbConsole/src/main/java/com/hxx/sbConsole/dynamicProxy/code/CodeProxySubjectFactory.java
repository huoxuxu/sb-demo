package com.hxx.sbConsole.dynamicProxy.code;

import com.hxx.sbConsole.service.ICodeProxySubject;
import com.hxx.sbConsole.service.impl.CodeProxySubjectImpl;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-18 18:37:40
 **/
public class CodeProxySubjectFactory {
    public static ICodeProxySubject getIns(){
        return new CodeProxySubject(new CodeProxySubjectImpl());
    }

    public static void main(String[] args) {
        ICodeProxySubject proxy=CodeProxySubjectFactory.getIns();
        proxy.doSomething("啦啦啦");
    }
}
