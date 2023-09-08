package com.hxx.sbConsole.service.impl.http;

import com.hxx.sbcommon.common.io.http.ApacheHttpClientSimpleUseful;
import com.hxx.sbcommon.common.io.http.client.HttpApiClient;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 14:56:54
 **/
@Slf4j
@var
public class BaiduHttpApiClient {
    private static Class CurrentCls = BaiduHttpApiClient.class;
    private static final String URL = "http://www.baidu.com";


    /**
     * 获取接口内容
     *
     * @return
     * @throws IOException
     */
    public static String getContent() throws IOException {
        String apiName = CurrentCls.getSimpleName() + ".getContent";
        var url = URL;
        String respJson = HttpApiClient.get(apiName, url);
        if (StringUtils.isBlank(respJson))
            throw new IllegalStateException(apiName + "，接口未响应数据");
        return respJson;
    }

    public static String getContent2() throws IOException {
        String apiName = CurrentCls.getSimpleName() + ".getContent2";
        String action = "/kss";
        var url = URL + action;
        String respJson = HttpApiClient.get(apiName, url);
        if (StringUtils.isBlank(respJson))
            throw new IllegalStateException(apiName + "，接口未响应数据");
        return respJson;
    }
}
