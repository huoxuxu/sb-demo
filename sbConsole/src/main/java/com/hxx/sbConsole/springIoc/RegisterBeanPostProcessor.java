package com.hxx.sbConsole.springIoc;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

/**
 * bean的动态注入
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-22 13:59:11
 **/
public class RegisterBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("school1");
        if (beanDefinition == null) {
            return;
        }

        String str = "schoolName";
        Object obj = beanDefinition.getAttribute(str);
        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.add(str, obj);
    }

}
