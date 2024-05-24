package com.hxx.sbConsole.module.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class DefaultRepository<T> implements Repository<T> , InvocationHandler {
    // 这里声明一个Class,用来接收接口声明的泛型实际类型的class,T是声明的实体类类型
    private Class<T> clazz;

    public DefaultRepository(Class<T> interfaceType) {
        // 获取当前类上的泛型类型
        ParameterizedType parameterizedType = (ParameterizedType) interfaceType.getGenericInterfaces()[0];
        // 获取泛型对应的真实类型(泛型真实类型在很多场合需要使用)
        Type[] actualType = parameterizedType.getActualTypeArguments();
        // 取数组的第一个，肯定是T的类型，即实体类类型(如果有多个，递增角标即可)
        this.clazz = (Class<T>) actualType[0];
    }

    @Override
    public String print() {
        // 示例方法的默认实现
        System.out.println(clazz);
        return clazz.getName();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Object 方法，走原生方法,比如hashCode()
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
        }
        // 其它走本地代理
        return method.invoke(this, args);
    }
}
