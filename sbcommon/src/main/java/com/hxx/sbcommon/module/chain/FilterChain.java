package com.hxx.sbcommon.module.chain;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:35:12
 **/
public interface FilterChain {

    void doFilter(HttpRequest httpRequest);


    void addFilter(Filter filter);
}
