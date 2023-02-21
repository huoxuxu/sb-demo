package com.hxx.sbrest.common.config;

import com.hxx.sbrest.common.interceptors.FirstInterceptor;
import com.hxx.sbrest.common.interceptors.LogInterceptor;
import com.hxx.sbrest.common.interceptors.SecondInterceptor;
import com.hxx.sbrest.common.interceptors.ThirdInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 15:11:12
 **/
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private FirstInterceptor firstInterceptor;
    @Autowired
    private SecondInterceptor secondInterceptor;
    @Autowired
    private ThirdInterceptor thirdInterceptor;

    @Autowired
    private LogInterceptor logInterceptor;


    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(firstInterceptor).addPathPatterns("/index/**");
        registry.addInterceptor(secondInterceptor).addPathPatterns("/index/**");
        registry.addInterceptor(thirdInterceptor).addPathPatterns("/index/**");

        registry.addInterceptor(logInterceptor);
    }


}
