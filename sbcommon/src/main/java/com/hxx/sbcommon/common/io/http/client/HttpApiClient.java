package com.hxx.sbcommon.common.io.http.client;

import com.hxx.sbcommon.common.io.http.ApacheHttpClientSimpleUseful;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求端
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 15:30:59
 **/
@Slf4j
public class HttpApiClient {

    /**
     * 发起get请求
     *
     * @param apiName
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String apiName, String url) throws IOException {
        return get(apiName, url, null);
    }

    /**
     * 发起get请求
     *
     * @param apiName
     * @param url
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String get(String apiName, String url, Map<String, String> headerMap) throws IOException {
        return send(apiName, HttpRequestTypeEnum.GET, url, headerMap, null);
    }

    /**
     * 发起post-application/json 请求
     *
     * @param apiName
     * @param url
     * @param headerMap
     * @param formBody
     * @return
     * @throws IOException
     */
    public static String postJson(String apiName, String url, Map<String, String> headerMap, String formBody) throws IOException {
        return send(apiName, HttpRequestTypeEnum.POST_JSON, url, headerMap, formBody);
    }

    /**
     * 发起post-application/x-www-form-urlencoded 请求
     *
     * @param apiName
     * @param url
     * @param headerMap
     * @param formBody
     * @return
     * @throws IOException
     */
    public static String postUrlEncoded(String apiName, String url, Map<String, String> headerMap, String formBody) throws IOException {
        return send(apiName, HttpRequestTypeEnum.POST_URL_ENCODED, url, headerMap, formBody);
    }

    /**
     * 发生Http请求
     *
     * @param apiName
     * @param httpRequestType
     * @param url
     * @param formBody
     * @return
     * @throws IOException
     */
    private static String send(String apiName, HttpRequestTypeEnum httpRequestType, String url, Map<String, String> headerMap, String formBody) throws IOException {
//            String apiName = CurrentCls.getSimpleName() + ".getContent";
        log.info(apiName + "，{} {} {}", url, formBody, JsonUtil.toJSON(headerMap));
        StopWatch sw = new StopWatch();
        sw.start();
        try {
            String respJson;
            // 核心业务
            switch (httpRequestType) {
                case GET:
                    respJson = ApacheHttpClientSimpleUseful.sendGet(url, headerMap, null);
                    break;
                case POST_JSON:
                    respJson = ApacheHttpClientSimpleUseful.sendPostJson(url, headerMap, formBody);
                    break;
                case POST_URL_ENCODED:
                    respJson = ApacheHttpClientSimpleUseful.sendPostUrlEncoded(url, headerMap, formBody);
                    break;
                case POST_MULTIPART_FORMDATA:

                    respJson = "";
                    break;
                case UNKNOWN:
                default:
                    throw new IllegalArgumentException("请求类型无效！");
            }
            if (sw.isRunning()) sw.stop();

            if (StringUtils.isBlank(respJson))
                throw new IllegalStateException(apiName + "，接口未响应数据");
            if (respJson.length() < 500)
                log.info(apiName + "，http耗时：{} 出参：{}", sw.getTotalTimeMillis(), respJson);
            else {
                log.debug(apiName + "，出参：{}", respJson);
                log.info(apiName + "，http耗时：{}", sw.getTotalTimeMillis());
            }
            return respJson;
        } catch (java.net.UnknownHostException
                 | org.apache.http.conn.ConnectTimeoutException
                 | java.net.SocketTimeoutException e) {
            if (sw.isRunning()) sw.stop();
            long cost = sw.getTotalTimeMillis();
            log.error("访问外部接口网络超时：{} [耗时：{}] {}", url, cost, ExceptionUtils.getStackTrace(e));
            throw new IllegalStateException("访问外部接口网络超时！[耗时：" + cost + "ms]", e);
        }
    }

    // 请求类型枚举、Get、Post-Json
    @Getter
    @AllArgsConstructor
    private enum HttpRequestTypeEnum {
        GET(0, "HttpGet"),
        POST_JSON(1, "HttpPostJson"),
        POST_URL_ENCODED(1, "HttpPostUrlEncoded"),
        POST_MULTIPART_FORMDATA(2, "HttpPostMultipartFormData"),
        UNKNOWN(-1, "");

        private final Integer code;

        private final String name;

        private static final Map<Integer, HttpRequestTypeEnum> codeLookup = new HashMap<>();

        static {
            for (HttpRequestTypeEnum val : values()) {
                codeLookup.put(val.code, val);
            }
        }

        /**
         * @param code
         * @return
         */
        public static HttpRequestTypeEnum getByCode(Integer code) {
            if (code == null) {
                return HttpRequestTypeEnum.UNKNOWN;
            }

            return codeLookup.getOrDefault(code, HttpRequestTypeEnum.UNKNOWN);
        }

        /**
         * 获取枚举名称，未匹配到，返回默认值
         *
         * @param code
         * @return
         */
        public static String getNameOrDefault(Integer code, String defaultName) {
            HttpRequestTypeEnum enumVal = getByCode(code);
            if (enumVal == HttpRequestTypeEnum.UNKNOWN) {
                return defaultName;
            }

            return enumVal.getName();
        }

        /**
         * 校验是否符合枚举
         *
         * @param val
         * @return
         */
        public static boolean check(Integer val) {
            if (val == null) {
                return false;
            }
            HttpRequestTypeEnum codeEnum = getByCode(val);
            return codeEnum != HttpRequestTypeEnum.UNKNOWN;
        }
    }
}
