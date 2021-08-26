package com.hxx.sbrest.common.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-30 10:06:02
 **/
@Component
public class MyFilterOne implements Filter {
    private String encoding;//存放要使用的编码格式

    public MyFilterOne() {
    }

    public void init(FilterConfig fConfig)
            throws ServletException {
        encoding = fConfig.getInitParameter("encoding");
    }

    public void destroy() {
        this.encoding = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (encoding != null) {
            request.setCharacterEncoding(encoding);//设置request字符编码
            request = new MyRequest((HttpServletRequest) request);
            //将传递的ServletRequest对象转化为自定义的Request对象，即可实现非法字符的过滤
            response.setContentType("text/html;charset=" + encoding);//设置response字符编码
        }
        chain.doFilter(request, response);
    }


}
