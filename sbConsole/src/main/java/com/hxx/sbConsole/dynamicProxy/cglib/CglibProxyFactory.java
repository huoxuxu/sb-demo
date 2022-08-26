package com.hxx.sbConsole.dynamicProxy.cglib;

import io.netty.handler.codec.MessageToMessageDecoder;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 15:42:13
 **/
public class CglibProxyFactory<T> implements MethodInterceptor {
    private Class<?> cls;
    private T target;

    public CglibProxyFactory(T target) {
        this.target = target;
    }

    public CglibProxyFactory(Class<?> cls) {
        this.cls = cls;
    }

    /**
     * 获取代理对象
     *
     * @return
     */
    public T getProxyObj() {
        // 1. 创建Enhancer类对象，它类似于咱们JDK动态代理中的Proxy类，该类就是用来获取代理对象的
        Enhancer enhancer = new Enhancer();
        // 2. 设置父类的字节码对象。为啥子要这样做呢？因为使用CGLIB生成的代理类是属于目标类的子类的，也就是说代理类是要继承自目标类的
        if (cls == null) {
            enhancer.setSuperclass(this.target.getClass());
        } else {
            enhancer.setSuperclass(this.cls);
        }
        // 3. 设置回调函数
        enhancer.setCallback(this);
        // 4. 创建代理对象
        return (T) enhancer.create();
    }

    /**
     * @param proxyObject
     * @param method
     * @param args
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object proxyObject, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before(method);

        Object invoke = null;
        if (this.target != null) {
            invoke = methodProxy.invoke(this.target, args);
        } else {
            // 无接口实例
            // 只能根据methodName，自定义处理了
        }

        after(method);
        return invoke;
    }

    /**
     * 调用invoke方法之前执行
     */
    private void before(Method method) {
        String methodName = method.getName();
        System.out.println("调用方法" + methodName + "之【前】的日志处理");
    }

    /**
     * 调用invoke方法之后执行
     */
    private void after(Method method) {
        String methodName = method.getName();
        System.out.println("调用方法" + methodName + "之【后】的日志处理");
    }

}