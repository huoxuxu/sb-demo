package com.hxx.sbConsole.dynamicProxy.jdk;

import com.hxx.sbConsole.service.Hello;
import com.hxx.sbcommon.common.reflect.ReflectUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * JDK代理
 * 有接口，可以使用JDK动态代理
 * 最小化依赖关系、代码实现简单、简化开发和维护、JDK原生支持，比CGLIB更加可靠，随JDK版本平滑升级
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-26 16:11:31
 **/
public class JDKDynamicProxyImplInterfaceTest {
    public static void main(String[] args) {
        Map<Method, MethodRunner> methodMap = new HashMap<>();
        // 添加MethodRunner
        {

        }

        case0(methodMap);
    }

    /**
     * jdk实现接口
     */
    static void case0(Map<Method, MethodRunner> methodMap) {
        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Object.class.equals(method.getDeclaringClass())) {
                    return method.invoke(this, args);
                }

                MethodRunner runner = methodMap.getOrDefault(method, null);
                return runner.exec(args);
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

    @lombok.Data
    static class MethodRunner {
        private Method method;
        /**
         * 方法签名
         */
        private String methodSignature;

        public MethodRunner(Method method) {
            this.method = method;
            this.methodSignature = ReflectUtil.getMethodSignature(method);
        }

        /**
         * @param args 方法待执行的参数
         * @return
         */
        public Object exec(Object[] args) {
            System.out.println("================start=================");
            System.out.println("method：" + methodSignature);
            if (method.getName()
                    .equals("morning")) {
                System.out.println("Good morning, " + args[0]);
            }

            System.out.println("================end=================");
            return null;
        }

    }

}
