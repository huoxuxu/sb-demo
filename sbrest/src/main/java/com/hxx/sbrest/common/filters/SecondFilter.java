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
public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("SecondFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("SecondFilter       doFilter pre");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("SecondFilter       doFilter aft");
    }

    @Override
    public void destroy() {
        System.out.println("SecondFilter destroy");
    }
}
