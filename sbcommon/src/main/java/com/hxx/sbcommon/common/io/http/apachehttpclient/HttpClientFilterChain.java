package com.hxx.sbcommon.common.io.http.apachehttpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientFilterChain {
    private final Map<String,Object> attachments = new HashMap<String,Object>();

    private int pos = 0;
    private List<HttpClientFilter> httpFilters= new ArrayList<HttpClientFilter>();

    public static HttpClientFilterChain build(List<HttpClientFilter> httpFilters){
        return new HttpClientFilterChain(httpFilters);
    }
    public HttpClientFilterChain(List<HttpClientFilter> httpFilters) {
        if(httpFilters != null)
            this.httpFilters.addAll(httpFilters);
    }
    public Object getAttachment(String key) {
        return this.attachments.get(key);
    }

    public HttpClientFilterChain setAttachment(String key, Object value) {
        if (value == null) {
            removeAttachment(key);
        } else {
            this.attachments.put(key, value);
        }

        return this;
    }

    public HttpClientFilterChain removeAttachment(String key) {
        this.attachments.remove(key);
        return this;
    }

    public Map<String, Object> getAttachments() {
        return this.attachments;
    }

    public void clearAttachments() {
        getAttachments().clear();
    }

    private List<HttpClientFilter> getFilters(){
        return this.httpFilters;
    }

    public void doFilter(HttpReqAndRsp httpReqAndRsp/* HttpUriRequest httpRequest , CloseableHttpResponse httpResponse*/){
        HttpClientFilter httpFilter = nextFilter();
        if(httpFilter!= null)
            httpFilter.doFilter(httpReqAndRsp,this);
        //httpFilter.doFilter(httpRequest,httpResponse,this);
    }
    private HttpClientFilter nextFilter() {
        if(pos < getFilters().size()){
            HttpClientFilter filter = getFilters().get(pos++);
            return filter;
        }else{
            pos = 0;
            return null;
        }
    }

    public void reset() {
        clearAttachments();
        pos = 0;

    }
}
