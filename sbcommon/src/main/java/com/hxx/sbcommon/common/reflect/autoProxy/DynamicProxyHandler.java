package com.hxx.sbcommon.common.reflect.autoProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 14:54:42
 **/
public class DynamicProxyHandler<T> implements InvocationHandler {

    private Object target;

    public DynamicProxyHandler(final Object target) {
        this.target = target;
    }

    /**
     * 获取代理对象
     *
     * @return
     */
    public T getProxyObject() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        //类可以实现多个接口，因此这里的接口是个数组
        Class<?>[] interfaces = target.getClass().getInterfaces();
        //this即MyInvocatioHandler实例，其包含被代理类的引用，以及重写的方法，newProxyInstance方法将利用这些参数创建一个代理类的实例
        return (T) Proxy.newProxyInstance(loader, interfaces, this);
    }

    /**
     * @param proxy  the proxy instance that the method was invoked on
     * @param method 代理的方法信息 the {@code Method} instance corresponding to
     *               the interface method invoked on the proxy instance.  The declaring
     *               class of the {@code Method} object will be the interface that
     *               the method was declared in, which may be a superinterface of the
     *               proxy interface that the proxy class inherits the method through.
     * @param args   代理方法传入的参数 an array of objects containing the values of the
     *               arguments passed in the method invocation on the proxy instance,
     *               or {@code null} if interface method takes no arguments.
     *               Arguments of primitive types are wrapped in instances of the
     *               appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行前1");
        Object result = method.invoke(target, args);
        System.out.println("执行后1");
        return result;
    }


}
