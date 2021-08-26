package com.hxx.sbrest.common.config;


import com.hxx.sbrest.common.filters.FirstFilter;
import com.hxx.sbrest.common.filters.MyFilterOne;
import com.hxx.sbrest.common.filters.SecondFilter;
import com.hxx.sbrest.common.filters.ThirdFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 15:05:57
 **/
@Configuration
public class FilterConfig {
    @Autowired
    private FirstFilter firstFilter;

    @Autowired
    private SecondFilter secondFilter;

    @Autowired
    private ThirdFilter thirdFilter;

    @Autowired
    private MyFilterOne myFilterOne;

    @Bean
    public FilterRegistrationBean filterRegistrationBeanFirst() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        // 可以采用 spring 的依赖注入 20191117update
        filterRegistrationBean.setFilter(firstFilter);
        filterRegistrationBean.addUrlPatterns("/index/*");
        filterRegistrationBean.setName("FirstFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBeanSecond() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(secondFilter);
        filterRegistrationBean.addUrlPatterns("/index/*");
        filterRegistrationBean.setName("SecondFilter");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBeanThird() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(thirdFilter);
        filterRegistrationBean.addUrlPatterns("/index/*");
        filterRegistrationBean.setName("ThirdFilter");
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBeanMyFilterOne() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(myFilterOne);
        //filterRegistrationBean.addUrlPatterns("/index/*");
        filterRegistrationBean.setName("MyFilterOne");
        filterRegistrationBean.setOrder(3);
        return filterRegistrationBean;
    }
}
