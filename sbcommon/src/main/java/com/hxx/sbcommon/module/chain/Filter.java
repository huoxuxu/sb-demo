package com.hxx.sbcommon.module.chain;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:33:31
 **/
public interface Filter {

    void doFilter(HttpRequest httpRequest, FilterChain filterChain);
}
