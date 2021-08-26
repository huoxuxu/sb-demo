package com.hxx.sbrest.common.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 15:09:15
 **/
@Component
public class SecondInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 在请求处理之前进行调用（Controller方法调用之前）
        System.out.println("SecondInterceptor  preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
            throws Exception {
        // 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后 )
        System.out.println("SecondInterceptor  postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex)
            throws Exception {
        // 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
        System.out.println("SecondInterceptor  afterCompletion");
    }

}
