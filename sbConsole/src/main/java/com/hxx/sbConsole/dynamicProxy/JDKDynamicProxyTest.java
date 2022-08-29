package com.hxx.sbConsole.dynamicProxy;

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

    static void case0() {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("================start=================");
                System.out.println("method：" + method);
                if (method.getName().equals("morning")) {
                    System.out.println("Good morning, " + args[0]);
                }

                System.out.println("================end=================");
                return null;
            }
        };

        Hello hello = (Hello) Proxy.newProxyInstance(
                // 传入ClassLoader
                Hello.class.getClassLoader(),
                // 传入要实现的接口
                new Class[]{Hello.class},
                // 传入处理调用方法的InvocationHandler
                handler);
        hello.morning("小小");
    }

    /**
     * JDK代理，JDKDynamicProxyHandler必须传入接口
     */
    static void case1() {
        Hello hello = new HelloImpl();
        JDKDynamicProxyHandler<Hello> proxyHandler = new JDKDynamicProxyHandler<>(hello);
        Hello proxyHello = proxyHandler.getProxy();
        proxyHello.morning("哈哈");
    }

}
