package com.hxx.sbcommon.apacheHttpClient;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpRequestRetryHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:24:25
 **/
public interface HttpInterceptor extends HttpRequestInterceptor, HttpResponseInterceptor, HttpRequestRetryHandler {
}