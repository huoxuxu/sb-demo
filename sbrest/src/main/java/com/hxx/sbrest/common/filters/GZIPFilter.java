package com.hxx.sbrest.common.filters;

import com.hxx.sbrest.common.utils.UnZIPRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-07-26 14:59:23
 **/
@Slf4j
@Component
public class GZIPFilter implements Filter {

    private static final String CONTENT_ENCODING = "Content-Encoding";
    private static final String CONTENT_ENCODING_TYPE = "gzip";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init GZIPFilter");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String encodeType = httpServletRequest.getHeader(CONTENT_ENCODING);
        if (CONTENT_ENCODING_TYPE.equals(encodeType)) {
            log.info("请求:{} 需要解压", httpServletRequest.getRequestURI());
            UnZIPRequestWrapper unZIPRequestWrapper = new UnZIPRequestWrapper(httpServletRequest);
            filterChain.doFilter(unZIPRequestWrapper, servletResponse);
        } else {
            log.info("请求:{} 无需解压", httpServletRequest.getRequestURI());
            filterChain.doFilter(servletRequest, servletResponse);
        }
        log.info("耗时：{}ms", System.currentTimeMillis() - start);
    }

    @Override
    public void destroy() {
        log.info("destroy GZIPFilter");
    }

}
