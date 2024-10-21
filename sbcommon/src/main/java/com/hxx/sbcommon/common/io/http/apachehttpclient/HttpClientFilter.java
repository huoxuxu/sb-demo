package com.hxx.sbcommon.common.io.http.apachehttpclient;

public interface HttpClientFilter {
    void doFilter(HttpReqAndRsp httpReqAndRsp, HttpClientFilterChain httpFilterChain);
}
