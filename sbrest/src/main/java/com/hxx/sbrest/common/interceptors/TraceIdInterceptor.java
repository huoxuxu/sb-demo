package com.hxx.sbrest.common.interceptors;

import com.hxx.sbcommon.common.other.traceId.TraceUtil;
import com.hxx.sbrest.webUtil.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 串联TraceId
 * 1、redirect重定向前后是两个不同的request，就算拦截器拦截到也是跳转后的request，没法串联上跳转前的traceId。
 * 2、forward转发前后是同一个request，但是不同于redirect重定向跳转，forward跳转系服务器内部跳转，不经过拦截器，依旧无法串联traceId。
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 14:43:58
 **/
public class TraceIdInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HttpServletRequestUtil.initTraceFromRequest(request);
        }
        return true;
    }
}
