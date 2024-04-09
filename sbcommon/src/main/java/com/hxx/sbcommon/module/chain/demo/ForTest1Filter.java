package com.hxx.sbcommon.module.chain.demo;

import com.hxx.sbcommon.module.chain.Filter;
import com.hxx.sbcommon.module.chain.FilterChain;
import com.hxx.sbcommon.module.chain.HttpRequest;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:34:08
 **/
public class ForTest1Filter implements Filter {
    @Override
    public void doFilter(HttpRequest httpRequest, FilterChain filterChain) {
        // do


        System.out.println(this.getClass().getSimpleName() + " before " + System.currentTimeMillis());


        filterChain.doFilter(httpRequest);


        // after


        System.out.println(this.getClass().getSimpleName() + " end " + System.currentTimeMillis());


    }

}
