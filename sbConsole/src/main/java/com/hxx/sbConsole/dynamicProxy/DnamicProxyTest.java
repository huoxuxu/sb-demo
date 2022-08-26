package com.hxx.sbConsole.dynamicProxy;

import com.hxx.sbConsole.dynamicProxy.cglib.CglibProxyFactory;
import com.hxx.sbConsole.dynamicProxy.cglib.LogInterceptor;
import com.hxx.sbConsole.service.impl.biz.HelloImpl;
import net.sf.cglib.proxy.Enhancer;

/**
 * 动态生成代理类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-25 16:16:00
 **/
public class DnamicProxyTest {
    public static void main(String[] args) {
        case2();
    }

    static void case2() {
        HelloImpl hello = new HelloImpl();
        CglibProxyFactory<HelloImpl> cglibProxy = new CglibProxyFactory<>(hello);
        HelloImpl proxyObj = cglibProxy.getProxyObj();
        proxyObj.morning("混合");
    }

    static void case3() {
        CglibProxyFactory<HelloImpl> cglibProxy = new CglibProxyFactory<>(HelloImpl.class);
        HelloImpl proxyObj = cglibProxy.getProxyObj();
        proxyObj.morning("简单");
    }

    static void case5() {
        LogInterceptor logInterceptor = new LogInterceptor();

        // 通过CGLIB动态代理获取代理对象的过程
        // 创建Enhancer对象，类似于JDK动态代理的Proxy类
        Enhancer enhancer = new Enhancer();
        // 设置目标类的字节码文件
        enhancer.setSuperclass(HelloImpl.class);
        // 设置回调函数
        enhancer.setCallback(logInterceptor);

        // create方法正式创建代理类
        HelloImpl hello = (HelloImpl) enhancer.create();

        // 调用代理类的具体业务方法
        hello.morning("法规");
    }

}
