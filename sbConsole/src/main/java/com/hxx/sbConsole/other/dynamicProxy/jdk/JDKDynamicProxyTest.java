package com.hxx.sbConsole.other.dynamicProxy.jdk;

import com.hxx.sbConsole.service.Hello;
import com.hxx.sbConsole.service.impl.biz.HelloImpl;
import com.hxx.sbcommon.common.reflect.autoProxy.JDKDynamicProxyHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK代理
 * 有接口，可以使用JDK动态代理
 * 最小化依赖关系、代码实现简单、简化开发和维护、JDK原生支持，比CGLIB更加可靠，随JDK版本平滑升级
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 16:11:31
 **/
public class JDKDynamicProxyTest {
    public static void main(String[] args) {
        case1();
    }


    /**
     * JDK代理，JDKDynamicProxyHandler必须传入接口
     */
    static void case1() {
        Hello hello = new HelloImpl();
        JDKDynamicProxyHandler<Hello> proxyHandler = new JDKDynamicProxyHandler<>(hello);
        Hello proxyHello = proxyHandler.getProxy();
        proxyHello.morning("哈哈");
        proxyHello.world("yaho");
    }

}
