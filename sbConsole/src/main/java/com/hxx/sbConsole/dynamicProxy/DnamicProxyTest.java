package com.hxx.sbConsole.dynamicProxy;

import com.hxx.sbConsole.service.Hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-25 16:16:00
 **/
public class DnamicProxyTest {
    public static void main(String[] args) {
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
                Hello.class.getClassLoader(), // 传入ClassLoader
                new Class[]{Hello.class}, // 传入要实现的接口
                handler); // 传入处理调用方法的InvocationHandler
        hello.morning("小小");
    }
}
