package com.hxx.springcommon.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 在普通类中使用IOC中的类
 * new 出来的类无法使用@Autowired注入Bean
 * Spring容器启动的时候，会把上下文环境对象调用实现ApplicationContextAware接口类中的setApplicationContext方法
 * 需要某一个bean的时候使用该类的ManageSpringBeans.getBean(beanName)来获取Spring的bean对象
 */
@Slf4j
@Component
public class SpringBeanActivatorUtil implements ApplicationContextAware {

    private static ApplicationContext context;


    /**
     * 获取applicationContext
     *
     * @return
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 通过name获取 Bean
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }

    public static <T> T getBeanOfType(Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBeanT(String beanName, Class<T> requiredType) {
        Object bean = getBean(beanName);
        return requiredType.cast(bean);
    }

    public static <T> Map<String, T> getBeansOfType(final Class<T> requiredType) {
        return context.getBeansOfType(requiredType);
    }

    public static Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
        return context.getBeansWithAnnotation(annotationType);
    }

    /**
     * 获取Bean名称数组
     *
     * @return
     */
    public String[] getBeanNames() {
        return context.getBeanDefinitionNames();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 通过这种形式获取Spring 本身的上下文内容
        SpringBeanActivatorUtil.context = applicationContext;
        log.info("SpringBeanActivatorUtil已设置applicationContext");
    }

}
