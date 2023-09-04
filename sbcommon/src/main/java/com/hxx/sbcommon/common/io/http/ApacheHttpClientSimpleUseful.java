package com.hxx.sbcommon.common.io.http;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * apache-http驱动
 * header信息，每次都需要重新写入请求
 * * <p>
 * * BA认证：
 * * urlParameters.add(new BasicNameValuePair("username", "abc"));
 * * urlParameters.add(new BasicNameValuePair("password", "123"));
 * * <p>
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-29 12:57:35
 **/
@Slf4j
public class ApacheHttpClientSimpleUseful {
    private static final String CONTENT_VALUE_FORM_DATA = "multipart/form-data";
    private static final String CONTENT_VALUE_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_VALUE_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-type";
    public static final String CHAR_SET_HEADER_KEY = "charset";
    public static final String CHAR_SET = "UTF-8";

    // 指定与远程主机建立连接的超时时间
    public static final int DEFAULT_CONNECTION_TIMEOUT = 10000;
    // 指定建立连接后等待数据的超时时间
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    // 指定从连接池获取连接的超时时间
    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000;

    public static final int maxConnectionSize = 500;
    public static final int maxPerRouteSize = 500;
    public static final int maxIdleSecond = 60;

    // Content-Type: text/plain; charset=UTF-8
    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);

    private static final CloseableHttpClient HTTP_CLIENT;

    /**
     * 发起 GET 请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     * @throws IOException
     */
    public static HttpApiResp sendGetResp(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        if (params != null) {
            // 追加到url中
            url = appendQueryString(url, params);
        }
//        RequestConfig requestConfig = getRequestConfig();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Content-type", HttpClientTools.getContentType(CONTENT_VALUE_FORM, CHAR_SET));
        log.debug("sendHttpGet url:{}", url);

        return HttpClientTools.execute(httpGet, header);
    }

    /**
     * 发起 GET 请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     * @throws IOException
     */
    public static String sendGet(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        try (HttpApiResp resp = sendGetResp(url, header, params)) {
            log.debug("sendHttpGet-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendHttpGet-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return HttpClientTools.processResp(resp);
        }
    }

    /**
     * 发起Get请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGet(String url) throws IOException {
        return sendGet(url, null, null);
    }

    /**
     * 发起 POST 请求
     *
     * @param url
     * @param header
     * @param json
     * @return
     * @throws IOException
     */
    public static HttpApiResp sendPostJsonResp(String url, Map<String, String> header, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        StringEntity stringEntity = new StringEntity(json, CHAR_SET);
        stringEntity.setContentEncoding(CHAR_SET);
        stringEntity.setContentType(CONTENT_VALUE_JSON);
        httpPost.setEntity(stringEntity);
        log.debug("sendJsonPost url={}, json={}", url, json);

        return HttpClientTools.execute(httpPost, header);
    }

    /**
     * 发起 POST 请求
     *
     * @param url
     * @param header
     * @param json
     * @return
     * @throws IOException
     */
    public static String sendPostJson(String url, Map<String, String> header, String json) throws IOException {
        try (HttpApiResp resp = sendPostJsonResp(url, header, json)) {
            log.debug("sendJsonPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendJsonPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return HttpClientTools.processResp(resp);
        }
    }

    /**
     * 发送Post请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     * @throws IOException
     */
    public static HttpApiResp sendPostUrlEncodedResp(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        if (!MapUtils.isEmpty(params)) {
            // 追加到url中
            List<NameValuePair> nvps = new ArrayList();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHAR_SET));
            log.debug("sendHttpUrlEncodedPost url={}, param={}", url, nvps);
        }
        httpPost.setHeader("Content-type", HttpClientTools.getContentType(CONTENT_VALUE_FORM, CHAR_SET));

        return HttpClientTools.execute(httpPost, header);
    }

    /**
     * 发送Post请求
     *
     * @param url
     * @param header
     * @param params
     * @return
     * @throws IOException
     */
    public static String sendPostUrlEncoded(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        try (HttpApiResp resp = sendPostUrlEncodedResp(url, header, params)) {
            log.debug("sendPostUrlEncoded-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendPostUrlEncoded-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return HttpClientTools.processResp(resp);
        }
    }

    // 处理QueryString
    private static String appendQueryString(String url, Map<String, String> params) throws IOException {
        if (params.isEmpty()) return url;

        List<NameValuePair> urlParams = new ArrayList(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String value = entry.getValue();
            if (value == null) continue;

            urlParams.add(new BasicNameValuePair(entry.getKey(), value));
        }

        String urlQs = EntityUtils.toString(new UrlEncodedFormEntity(urlParams, CHAR_SET));
        String appChar = url.contains("?") ? "" : "?";
        url = url + appChar + urlQs;
        return url;
    }

    static {
        HTTP_CLIENT = HttpClientTools.createHttpClient(maxConnectionSize, maxPerRouteSize, maxIdleSecond,
                DEFAULT_CONNECTION_REQUEST_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_SOCKET_TIMEOUT,
                null, 0);
    }

    /**
     * 获取ApacheHttpClient
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        return HTTP_CLIENT;
    }

    /**
     * ApacheHttpClient工具类
     */
    public static class HttpClientTools {
        /**
         * 获取ContentType
         *
         * @param contentType application/json
         * @param charSet     utf-8
         * @return
         */
        public static String getContentType(String contentType, String charSet) {
            return MessageFormat.format("{0};charset={1}", contentType, charSet);
        }

        /**
         * 创建 Http 驱动
         *
         * @param poolMaxTotal             连接池里的最大连接数
         * @param poolDefaultMaxPerRoute   某一个/每服务每次能并行接收的请求数量
         * @param maxIdleSecond
         * @param connectionRequestTimeout 请求超时时间
         * @param connectTimeout           从连接池里获取连接超时时间
         * @param socketTimeout            读超时时间
         * @return
         */
        public static CloseableHttpClient createHttpClient(int poolMaxTotal, int poolDefaultMaxPerRoute, int maxIdleSecond,
                                                           int connectionRequestTimeout, int connectTimeout, int socketTimeout,
                                                           String proxyHost, int proxyPort) {
            RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
            ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
            registryBuilder.register("http", plainSF);
            // 指定信任密钥存储对象和连接套接字工厂
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                // 信任任何链接
                TrustStrategy anyTrustStrategy = (x509Certificates, s) -> true;
                SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
                LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                registryBuilder.register("https", sslSF);
            } catch (Exception e) {
                log.error("CreateHttpClientException", e);
                throw new RuntimeException(e);
            }

            Registry<ConnectionSocketFactory> registry = registryBuilder.build();
            //设置连接管理器
            PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
            // 所有路由允许的最大连接数.
            connManager.setMaxTotal(poolMaxTotal);
            // 未通过调用 setMaxPerRoute 指定的路由所允许的最大连接数
            connManager.setDefaultMaxPerRoute(poolDefaultMaxPerRoute);
            RequestConfig requestConfig = RequestConfig.custom()
                    // 在抛出异常之前尝试从连接池签出连接时要等待多长时间(例如，如果所有连接都被签出，则连接池不会立即返回).
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    // 在抛出超时异常之前等待与远程服务器建立连接的时间
                    .setConnectTimeout(connectTimeout)
                    // 在抛出超时异常之前等待服务器响应各种调用的时间.
                    .setSocketTimeout(socketTimeout)
                    .build();

            // 代理
            DefaultProxyRoutePlanner routePlanner = null;
            if (!StringUtils.isBlank(proxyHost)) {
                HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
                routePlanner = new DefaultProxyRoutePlanner(proxy);
            }
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    // 代理
                    .setRoutePlanner(routePlanner)
                    // 连接池
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    // 驱逐闲置连接
                    .evictIdleConnections(maxIdleSecond, TimeUnit.SECONDS)
                    .build();

            return httpClient;
        }

        /**
         * 执行 http 请求
         *
         * @param request
         * @param header
         * @return
         * @throws IOException
         */
        public static HttpApiResp execute(HttpUriRequest request, Map<String, String> header) throws IOException {
            // 处理header
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    request.setHeader(entry.getKey(), entry.getValue());
                }
            }
            Header connection = request.getFirstHeader("Connection");
            if (connection == null) {
                request.addHeader("Connection", "close");
            }
            // 发起请求
            CloseableHttpResponse response = HTTP_CLIENT.execute(request);
            HttpApiResp resp = new HttpApiResp(response, true);
            return resp;
        }

        /**
         * 处理响应
         *
         * @param resp
         * @return
         * @throws IOException
         */
        public static String processResp(HttpApiResp resp) throws IOException {
            int capacity = (int) resp.getContentLength();
            if (capacity < 0) capacity = 4096;

            try (InputStream inStream = resp.getNetInputStream()) {
                Reader reader = new InputStreamReader(inStream, CHAR_SET);
                CharArrayBuffer buffer = new CharArrayBuffer(capacity);
                char[] tmp = new char[16 * 1024];

                int len;
                while ((len = reader.read(tmp)) != -1) {
                    buffer.append(tmp, 0, len);
                }

                return buffer.toString();
            }
        }
    }

    /**
     * Http响应对象
     */
    @Data
    public static class HttpApiResp implements Closeable {

        private CloseableHttpResponse response;
        private HttpEntity entity;

        /**
         * 是否请求成功，httpstatuscode >=200 && <300
         */
        private boolean ok;

        /**
         * 状态码
         */
        private int statusCode;
        /**
         * 内容长度
         */
        private long contentLength;
        /**
         * 网络响应流
         */
        private InputStream netInputStream;
        /**
         * 是否自动关闭响应
         */
        private boolean autoCloseResponse;

        public HttpApiResp() {
        }

        /**
         * @param response
         * @param autoCloseResponse 是否自动关闭响应
         * @throws IOException
         */
        public HttpApiResp(CloseableHttpResponse response, boolean autoCloseResponse) throws IOException {
            this.response = response;
            this.autoCloseResponse = autoCloseResponse;
            // 获取响应体
            this.entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            this.ok = statusCode >= 200 && statusCode < 300;
            this.statusCode = statusCode;
            if (this.ok) {
                this.netInputStream = entity.getContent();
            }
            this.contentLength = entity.getContentLength();
        }

        @Override
        public void close() throws IOException {
            if (this.netInputStream != null) {
                this.netInputStream.close();
            }
            if (autoCloseResponse && this.response != null) {
                this.response.close();
            }
        }
    }
}
