package com.hxx.sbcommon.module.chain.demo;

import com.hxx.sbcommon.module.chain.FilterChain;
import com.hxx.sbcommon.module.chain.StandardFilterChain;
import com.hxx.sbcommon.module.chain.StandardHttpRequest;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:36:48
 **/
public class FilterChainClient {
    public static void main(String[] args) {
        FilterChain filterChain = new StandardFilterChain();

        // 添加过滤器
        filterChain.addFilter(new ForTest1Filter());
        filterChain.addFilter(new ForTest2Filter());

        // 执行过滤
        filterChain.doFilter(new StandardHttpRequest());
    }
}
