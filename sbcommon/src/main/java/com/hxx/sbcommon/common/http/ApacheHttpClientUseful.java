package com.hxx.sbcommon.common.http;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
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
public class ApacheHttpClientUseful {
    private static final String CONTENT_VALUE_FORM_DATA = "multipart/form-data";
    private static final String CONTENT_VALUE_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_VALUE_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-type";
    public static final String CHAR_SET_HEADER_KEY = "charset";
    public static final String CHAR_SET = "UTF-8";

    // 指定与远程主机建立连接的超时时间
    public static final Integer DEFAULT_CONNECTION_TIMEOUT = 5000;
    // 指定建立连接后等待数据的超时时间
    public static final Integer DEFAULT_SOCKET_TIMEOUT = 5000;
    // 指定从连接池获取连接的超时时间
    public static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000;

    public static final int maxConnectionSize = 500;
    public static final int maxPerRouteSize = 500;
    public static final int maxIdleSecond = 60;

    // Content-Type: text/plain; charset=UTF-8
    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);

    private static PoolingHttpClientConnectionManager cm;
    private static final CloseableHttpClient HTTP_CLIENT;

    public static HttpApiResp sendHttpGetResp(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        if (params != null) {
            // 追加到url中
            url = appendQueryString(url, params);
        }
//        RequestConfig requestConfig = getRequestConfig();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Content-type", getContentType(CONTENT_VALUE_FORM, CHAR_SET));
        log.debug("sendHttpGet url:{}", url);

        return execute(httpGet, header);
    }

    public static String sendHttpGet(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        try (HttpApiResp resp = sendHttpGetResp(url, header, params)) {
            log.debug("sendHttpGet-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendHttpGet-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return processResp(resp);
        }
    }

    public static String sendHttpGet(String url) throws IOException {
        return sendHttpGet(url, null, null);
    }

    public static HttpApiResp sendHttpUrlEncodedPostResp(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        if (params != null && !params.isEmpty()) {
            // 追加到url中
            List<NameValuePair> nvps = new ArrayList();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, CHAR_SET));
            log.debug("sendHttpUrlEncodedPost url={}, param={}", url, nvps);
        }
        httpPost.setHeader("Content-type", getContentType(CONTENT_VALUE_FORM, CHAR_SET));

        return execute(httpPost, header);
    }

    public static HttpApiResp sendJsonPostResp(String url, Map<String, String> header, String json) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        StringEntity stringEntity = new StringEntity(json, CHAR_SET);
        stringEntity.setContentEncoding(CHAR_SET);
        stringEntity.setContentType(CONTENT_VALUE_JSON);
        httpPost.setEntity(stringEntity);
        log.debug("sendJsonPost url={}, json={}", url, json);

        return execute(httpPost, header);
    }

    public static String sendHttpUrlEncodedPost(String url, Map<String, String> header, Map<String, String> params) throws IOException {
        try (HttpApiResp resp = sendHttpUrlEncodedPostResp(url, header, params)) {
            log.debug("sendHttpUrlEncodedPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendHttpUrlEncodedPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return processResp(resp);
        }
    }

    public static String sendJsonPost(String url, Map<String, String> header, String json) throws IOException {
        try (HttpApiResp resp = sendJsonPostResp(url, header, json)) {
            log.debug("sendJsonPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendJsonPost-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return processResp(resp);
        }
    }

    public static String sendFileUpload(String url, Map<String, String> header, Map<String, String> param, String name, File... files) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setCharset(Charset.forName(CHAR_SET));
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 写入文本
        if (param != null) {
            param.forEach((key, value) -> {
                mEntityBuilder.addTextBody(key, value, TEXT_PLAIN_UTF_8);
            });
        }

        List<FileInputStream> fls = new ArrayList<>();
        try {
            // 写入文件
            for (File file : files) {
                // 123.abc
                String filename = file.getName();
                FileInputStream fis = new FileInputStream(file);
                fls.add(fis);
                mEntityBuilder.addBinaryBody(name, fis, ContentType.MULTIPART_FORM_DATA, filename);

//        if (file instanceof byte[]) {
//
//        } else if (file instanceof InputStream) {
//            mEntityBuilder.addBinaryBody(name, (InputStream) file, ContentType.MULTIPART_FORM_DATA, filename);
//        }
            }

            httpPost.setEntity(mEntityBuilder.build());
            log.debug("sendFileUpload url={}, param={}", url, param);

            try (HttpApiResp resp = execute(httpPost, header)) {
                log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                if (!resp.ok) {
                    log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                    return null;
                }

                return processResp(resp);
            }
        } finally {
            for (FileInputStream item : fls) {
                try {
                    item.close();
                } catch (Exception ignore) {

                }
            }
        }
    }

    public static String sendFileUpload(String url, Map<String, String> header, Map<String, String> param, String name, String[] fileNames, InputStream... files) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setCharset(Charset.forName(CHAR_SET));
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 写入文本
        param.forEach((key, value) -> {
            mEntityBuilder.addTextBody(key, value, TEXT_PLAIN_UTF_8);
        });

        // 写入文件
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            InputStream file = files[i];
            mEntityBuilder.addBinaryBody(name, file, ContentType.MULTIPART_FORM_DATA, fileName);
        }

        httpPost.setEntity(mEntityBuilder.build());
        log.debug("sendFileUpload url={}, param={}", url, param);

        try (HttpApiResp resp = execute(httpPost, header)) {
            log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return processResp(resp);
        }
    }

    public static String sendFileUpload(String url, Map<String, String> header, Map<String, String> param, String name, String[] fileNames, byte[]... files) throws IOException {
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
        mEntityBuilder.setCharset(Charset.forName(CHAR_SET));
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        // 写入文本
        param.forEach((key, value) -> {
            mEntityBuilder.addTextBody(key, value, TEXT_PLAIN_UTF_8);
        });

        // 写入文件
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            byte[] file = files[i];
            mEntityBuilder.addBinaryBody(name, file, ContentType.MULTIPART_FORM_DATA, fileName);
        }

        httpPost.setEntity(mEntityBuilder.build());
        log.debug("sendFileUpload url={}, param={}", url, param);

        try (HttpApiResp resp = execute(httpPost, header)) {
            log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.ok) {
                log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return processResp(resp);
        }
    }

    /**
     * 获取请求配置
     *
     * @param connectTimeout           指定与远程主机建立连接的超时时间
     * @param socketTimeout            指定建立连接后等待数据的超时时间
     * @param connectionRequestTimeout 指定从连接池获取连接的超时时间
     * @return
     */
    public static RequestConfig getRequestConfig(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        return RequestConfig.custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
    }

    public static RequestConfig getRequestConfig(int connectTimeout, int socketTimeout) {
        return getRequestConfig(connectTimeout, socketTimeout, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
    }

    public static RequestConfig getRequestConfig() {
        return getRequestConfig(DEFAULT_CONNECTION_TIMEOUT, DEFAULT_SOCKET_TIMEOUT, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
    }

    private static String processResp(HttpApiResp resp) throws IOException {
        int capacity = (int) resp.getContentLength();
        if (capacity < 0) capacity = 4096;

        InputStream inStream = resp.getNetInputStream();
        Reader reader = new InputStreamReader(inStream, CHAR_SET);
        CharArrayBuffer buffer = new CharArrayBuffer(capacity);
        char[] tmp = new char[16 * 1024];

        int len;
        while ((len = reader.read(tmp)) != -1) {
            buffer.append(tmp, 0, len);
        }

        return buffer.toString();
    }

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

    private static String getContentType(String contentType, String charSet) {
        return MessageFormat.format("{0};charset={1}", contentType, charSet);
    }

    private static HttpApiResp execute(HttpUriRequest request, Map<String, String> header) throws IOException {
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
        HttpApiResp resp = new HttpApiResp(response);
        return resp;
    }

    static {
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxConnectionSize);
        cm.setDefaultMaxPerRoute(maxPerRouteSize);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(cm)
                // 驱逐闲置连接
                .evictIdleConnections(maxIdleSecond, TimeUnit.SECONDS);

//        Set<String> httpInterceptors = PluginConfigManager.getPropertyValueSet("com.zto.titans.soa.http.interceptor.HttpInterceptor");
//        if (httpInterceptors != null && !httpInterceptors.isEmpty()) {
//            Iterator var2 = httpInterceptors.iterator();
//
//            while (var2.hasNext()) {
//                String interceptor = (String) var2.next();
//
//                try {
//                    HttpInterceptor httpInterceptor = (HttpInterceptor) Class.forName(interceptor).newInstance();
//                    httpClientBuilder.addInterceptorFirst(httpInterceptor);
//                    httpClientBuilder.addInterceptorFirst(httpInterceptor);
//                    httpClientBuilder.setRetryHandler(httpInterceptor);
//                } catch (Exception var5) {
//                    throw FrameworkException.getInstance(var5, "Http加载插件异常,请检查配置文件[{0}]", new Object[]{"com.zto.titans.soa.http.interceptor.HttpInterceptor"});
//                }
//            }
//        }

        HTTP_CLIENT = httpClientBuilder.build();
//        List<PluginDecorator<Class>> orderedPluginClasses = PluginConfigManager.getOrderedPluginClasses("com.zto.titans.soa.http.filter.HttpClientFilter", true);
//        if (orderedPluginClasses != null) {
//            orderedPluginClasses.forEach((o) -> {
//                try {
//                    HttpClientFilter filter = (HttpClientFilter) Class.forName(o.getPlugin().getName()).newInstance();
//                    filterList.add(filter);
//                } catch (Exception var2) {
//                    throw FrameworkException.getInstance(var2, "Http加载插件异常,请检查配置文件[{0}]", new Object[]{"com.zto.titans.soa.http.filter.HttpClientFilter"});
//                }
//            });
//        }
    }

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

        //public HttpApiResp(){}

        public HttpApiResp(CloseableHttpResponse response) throws IOException {
            this.response = response;
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
            if (this.response != null) {
                this.response.close();
            }
        }
    }
}
