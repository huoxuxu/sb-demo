package com.hxx.sbweb.common.global;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description: new 出来的类无法使用@Autowired注入Bean
 * Spring容器启动的时候，会把上下文环境对象调用实现ApplicationContextAware接口类中的setApplicationContext方法
 * 需要某一个bean的时候使用该类的ManageSpringBeans.getBean(beanName)来获取Spring的bean对象
 * @Date: 2021-04-27 17:50:04
 **/
@Service
public class ManageSpringBeans implements ApplicationContextAware {
    private static ApplicationContext context;

    public static <T> T getBean(final Class<T> requiredType) {
        return context.getBean(requiredType);
    }

    public static <T> T getBean(final String beanName) {
        @SuppressWarnings("unchecked")
        final T bean = (T) context.getBean(beanName);
        return bean;
    }

    public static <T> Map<String, T> getBeans(final Class<T> requiredType) {
        return context.getBeansOfType(requiredType);
    }

    public static Map<String, Object> getBeansWithAnnotation(final Class<? extends Annotation> annotationType) {
        return context.getBeansWithAnnotation(annotationType);
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        context = applicationContext;
    }
}
