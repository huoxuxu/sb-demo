package com.hxx.sbcommon.apacheHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:27:47
 **/
public class HttpReqAndRsp {
    private HttpUriRequest httpRequest;
    private HttpResponse httpResponse;

    public HttpReqAndRsp(HttpUriRequest httpRequest, HttpResponse httpResponse) {
        setHttpRequest(httpRequest);
        setHttpResponse(httpResponse);
    }

    public HttpUriRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpUriRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }
}