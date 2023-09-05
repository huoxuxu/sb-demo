package com.hxx.sbcommon.common.io.http.client;

import com.hxx.sbcommon.common.io.http.ApacheHttpClientSimpleUseful;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 15:30:59
 **/
@Slf4j
public class HttpApiClient {
    /**
     * get
     *
     * @return
     * @throws IOException
     */
    public static String get(String apiName, String url) throws IOException {
//            String apiName = CurrentCls.getSimpleName() + ".getContent";
        log.info(apiName + "，请求入参：{}", "");
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            // 核心业务
            String respJson = ApacheHttpClientSimpleUseful.sendGet(url);
            log.debug(apiName + "，出参：{}", respJson);
            if (StringUtils.isBlank(respJson))
                throw new IllegalStateException(apiName + "，接口未响应数据");

            return respJson;
        } catch (java.net.UnknownHostException | org.apache.http.conn.ConnectTimeoutException e) {
            log.error("访问外部接口网络超时：{} {}", url, ExceptionUtils.getStackTrace(e));
            throw new IllegalStateException("访问外部接口网络超时！", e);
        } finally {
            sw.stop();
            log.info(apiName + "，http耗时：{}", sw.getTotalTimeMillis());
        }
    }

    /**
     * postJson
     *
     * @param apiName
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String postJson(String apiName, String url, String json) throws IOException {
        //            String apiName = CurrentCls.getSimpleName() + ".getContent";
        log.info(apiName + "，请求入参：{}", "");
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            // 核心业务
            String respJson = ApacheHttpClientSimpleUseful.sendPostJson(url, null, json);
            log.debug(apiName + "，出参：{}", respJson);
            if (StringUtils.isBlank(respJson))
                throw new IllegalStateException(apiName + "，接口未响应数据");

            return respJson;
        } catch (java.net.UnknownHostException | org.apache.http.conn.ConnectTimeoutException e) {
            log.error("访问外部接口网络超时：{} {}", url, ExceptionUtils.getStackTrace(e));
            throw new IllegalStateException("访问外部接口网络超时！", e);
        } finally {
            sw.stop();
            log.info(apiName + "，http耗时：{}", sw.getTotalTimeMillis());
        }
    }

}
