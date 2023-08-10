package com.hxx.sbrest.common.config;

import com.hxx.sbrest.common.filters.GZIPFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-07-26 15:03:12
 **/
@Configuration
public class FilterRegistration {

    @Resource
    private GZIPFilter gzipFilter;

    @Bean
    public FilterRegistrationBean<GZIPFilter> gzipFilterRegistrationBean() {
        FilterRegistrationBean<GZIPFilter> registration = new FilterRegistrationBean<>();
        //Filter可以new，也可以使用依赖注入Bean
        registration.setFilter(gzipFilter);
        //过滤器名称
        registration.setName("gzipFilter");
        //拦截路径
        registration.addUrlPatterns("/*");
        //设置顺序
        registration.setOrder(1);
        return registration;
    }

}
