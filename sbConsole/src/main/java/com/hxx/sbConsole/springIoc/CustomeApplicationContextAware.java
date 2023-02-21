package com.hxx.sbConsole.springIoc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-22 13:50:08
 **/
public class CustomeApplicationContextAware implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 通过这种形式获取Spring 本身的上下文内容
        CustomeApplicationContextAware.applicationContext = applicationContext;
    }

    /**
     * 获取Bean个数
     *
     * @return
     */
    public int getBeanCount() {
        return applicationContext.getBeanDefinitionCount();
    }

    /**
     * 获取Bean名称数组
     *
     * @return
     */
    public String[] getBeanNames() {
        return applicationContext.getBeanDefinitionNames();
    }

}
