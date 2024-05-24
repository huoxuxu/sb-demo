package com.hxx.sbConsole.module.dynamicproxy;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @ClassName RepositoryFactory
 * @Description FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例,其返回的是该工厂Bean的getObject方法所返回的对象
 * @Author Silwings
 * @Date 2021/3/7 16:01
 * @Version V1.0
 **/
public class RepositoryFactory<T> implements FactoryBean<T> {

    /**
     * 构建DefaultRepository需要使用的参数
     */
    private Class<T> interfaceType;

    public RepositoryFactory(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() throws Exception {
        // 因为DefaultRepository需要Class<T>作为参数,所以该类包含一个Claa<T>的成员,通过构造函数初始化
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                new DefaultRepository<>(interfaceType)
        );
    }

    @Override
    public Class<?> getObjectType() {
        // 该方法返回的getObject()方法返回对象的类型,这里是基于interfaceType生成的代理对象,所以类型就是interfaceType
        return interfaceType;
    }
}
