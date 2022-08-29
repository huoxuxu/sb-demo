package com.hxx.sbcommon.common.reflect.autoProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 14:54:42
 **/
public class JDKDynamicProxyHandler<T> implements InvocationHandler {
    /**
     *
     */
    private final T target;

    public JDKDynamicProxyHandler(final T target) {
        this.target = target;
    }


    /**
     * 获取代理对象
     *
     * @return
     */
    public T getProxy() {
        Object t = getProxyObject();
        return (T) t;
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
        Object result = method.invoke(this.target, args);
        System.out.println("执行后1");
        return result;
    }

    // 获取代理对象
    private Object getProxyObject() {
        ClassLoader loader = this.target.getClass().getClassLoader();
        //类可以实现多个接口，因此这里的接口是个数组
        Class<?>[] interfaces = target.getClass().getInterfaces();
        //this即MyInvocatioHandler实例，其包含被代理类的引用，以及重写的方法，newProxyInstance方法将利用这些参数创建一个代理类的实例
        Object t = Proxy.newProxyInstance(loader, interfaces, this);
        return t;
    }

}
