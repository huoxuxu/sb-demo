package com.hxx.sbrest.common.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 17:30:32
 **/
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.err.println("================================== preHandle1 ===========================================");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
        System.err.println("================================== postHandle1 ===========================================");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex)
            throws Exception {
        System.err.println("================================== afterCompletion1 ===========================================");
    }

}
