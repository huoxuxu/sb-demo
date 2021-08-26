package com.hxx.sbrest.common.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 15:01:32
 **/
@Component
public class ThirdFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("ThirdFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("ThirdFilter       doFilter pre");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("ThirdFilter       doFilter aft");
    }

    @Override
    public void destroy() {
        System.out.println("ThirdFilter destroy");
    }
}
