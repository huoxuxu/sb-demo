//package com.hxx.sbweb.common.filters;
//
//
//import javax.servlet.*;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2023-09-21 13:13:00
// **/
//@Component
//@WebFilter(filterName = "RequestWrapperFilter", urlPatterns = "/*")
//public class RequestWrapperFilter implements Filter {
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        ServletRequest requestWrapper = null;
//        if (request instanceof HttpServletRequest) {
//            requestWrapper = new RequestWrapper((HttpServletRequest) request);
//        }
//        if (null == requestWrapper) {
//            chain.doFilter(request, response);
//        } else {
//            chain.doFilter(requestWrapper, response);
//        }
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}