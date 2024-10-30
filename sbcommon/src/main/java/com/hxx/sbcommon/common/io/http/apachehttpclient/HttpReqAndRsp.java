package com.hxx.sbcommon.common.io.http.apachehttpclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-09 15:27:47
 **/
@Data
@AllArgsConstructor
public class HttpReqAndRsp {
    private HttpUriRequest httpRequest;
    private HttpResponse httpResponse;

}
