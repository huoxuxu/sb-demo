package com.hxx.sbConsole.springIoc;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Iterator;

/**
 * 动态修改bean信息
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-22 14:04:32
 **/
public class CustomBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 获取所有bean信息
        {
            Iterator<String> it = beanFactory.getBeanNamesIterator();
            while (it.hasNext()) {
                String next = it.next();
                System.out.println("next:" + next);
            }

            int count = beanFactory.getBeanDefinitionCount();
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
                System.out.println(beanDefinitionName + "" + beanDefinition.getPropertyValues());
            }
        }

        // 动态修改bean信息
        {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition("student");
            if (beanDefinition == null) {
                return;
            }

            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
            propertyValues.add("factoryInvoke", "through beanFactoryPostProcessor invoke");
        }
    }
}
