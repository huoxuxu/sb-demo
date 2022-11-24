package com.hxx.sbcommon.common.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http驱动，
 * 超时、请求头、GET、POST、上传
 * <p>
 * BA认证：
 * urlParameters.add(new BasicNameValuePair("username", "abc"));
 * urlParameters.add(new BasicNameValuePair("password", "123"));
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-24 14:08:56
 **/
@Slf4j
public class HttpClientUtil {
    private static final String CONTENT_TYPE = "Content-Type";

    private static final CloseableHttpClient httpClient;
    // 设置超时时间
    public static final int REQUEST_TIMEOUT = 1000;
    public static final int REQUEST_SOCKET_TIME = 60000;

    static {
        httpClient = buildHttpClient();
    }


    /**
     * get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        return get(url, null, null);
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param charset
     * @return
     * @throws Exception
     */
    public static String get(String url, Map<String, String> headers, Charset charset) throws Exception {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        final HttpGet httpGet = new HttpGet(url);
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                httpPost.addHeader("Content-Type", "application/json");
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        try (CloseableHttpResponse resp = httpClient.execute(httpGet)) {
            return processHttpResp(resp, charset);
        }
    }

    /**
     * post请求发送Json
     *
     * @param url
     * @param json
     * @param headers
     * @param charset
     * @return
     * @throws Exception
     */
    public static String postJson(String url, String json, Map<String, String> headers, Charset charset) throws Exception {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        StringEntity reqEntity = new StringEntity(json, charset);
        headers = headers == null ? new HashMap<>() : headers;
        if (!hasContentType(headers)) {
            headers.put("Content-Type", "application/json");
        }

        return post(url, reqEntity, headers, charset);
    }

    /**
     * post请求发送表单数据
     *
     * @param url
     * @param formParaMap
     * @param headers
     * @param charset
     * @return
     * @throws Exception
     */
    public static String postFormParam(String url, Map<String, String> formParaMap, Map<String, String> headers, Charset charset) throws Exception {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        headers = headers == null ? new HashMap<>() : headers;
        if (!hasContentType(headers)) {
            headers.put("Content-Type", "application/x-www-form-urlencoded");
        }

        List<NameValuePair> pairs = new ArrayList<>();
        if (formParaMap != null && !formParaMap.isEmpty()) {
            for (Map.Entry<String, String> entry : formParaMap.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(pairs, charset);
        return post(url, reqEntity, headers, charset);
    }

//    public static String postFormTxt(String url, String formTxt, String contentType, Map<String, String> headers, Charset charset) throws Exception {
//        if (charset == null) {
//            charset = StandardCharsets.UTF_8;
//        }
//
//        headers = headers == null ? new HashMap<>() : headers;
//        if (!hasContentType(headers)) {
//            headers.put("Content-Type", contentType);
//        }
//
//        StringEntity reqEntity = new StringEntity(formTxt, charset);
//        return post(url, reqEntity, headers, charset);
//    }

    /**
     * post请求
     *
     * @param url
     * @param reqEntity
     * @param headers
     * @param charset
     * @return
     * @throws Exception
     */
    public static String post(String url, HttpEntity reqEntity, Map<String, String> headers, Charset charset) throws Exception {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }

        final HttpPost httpPost = new HttpPost(url);
        // 添加请求头
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                httpPost.addHeader("Content-Type", "application/json");
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // 添加数据
        if (reqEntity != null) {
            httpPost.setEntity(reqEntity);
        }

        try (CloseableHttpResponse resp = httpClient.execute(httpPost)) {
            return processHttpResp(resp, charset);
        }
    }

    // 构造HttpClient
    private static CloseableHttpClient buildTimeoutHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(5000);
        cm.setDefaultMaxPerRoute(500);

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(REQUEST_SOCKET_TIME)
                .build();

        return HttpClients.custom()
                .setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig)
                //.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))
                .disableRedirectHandling()
                .build();
    }

    // 构造HttpClient
    private static CloseableHttpClient buildHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", new PlainConnectionSocketFactory());

        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom()
                    .useTLS()
                    .loadTrustMaterial(trustStore, anyTrustStrategy)
                    .build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(6000);
        connManager.setDefaultMaxPerRoute(1000);
        // 请求配置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(REQUEST_SOCKET_TIME)
                .build();
        // 构造连接
        return HttpClientBuilder.create()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    /**
     * 将map转换为请求字符串
     * data=xxx&msg_type=xxx
     *
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder data = new StringBuilder();
        boolean flag = false;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (flag) {
                data.append("&");
            } else {
                flag = true;
            }
            data.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(StringUtils.isBlank(entry.getValue()) ? "" : entry.getValue(), charset));
        }

        return data.toString();
    }

    // 是否包含ContentType
    private static boolean hasContentType(Map<String, String> headers) {
        boolean hasContentTypeFlag = false;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (CONTENT_TYPE.equalsIgnoreCase(entry.getKey())) {
                hasContentTypeFlag = true;
                break;
            }
        }
        return hasContentTypeFlag;
    }

    // 处理响应
    private static String processHttpResp(CloseableHttpResponse resp, Charset charset) throws IOException {
        StatusLine statusLine = resp.getStatusLine();
        if (statusLine == null) {
            throw new ClientProtocolException("读取Http响应信息为空");
        }

        final int statusCode = statusLine.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
//            httpPost.abort();
            throw new ClientProtocolException("非预期的Http响应码：" + statusCode);
        }

        final HttpEntity httpEntity = resp.getEntity();
        String html;
        if (charset != null) {
            html = EntityUtils.toString(httpEntity, charset);
        } else {
            html = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
        }

//        EntityUtils.consume(httpEntity);
        return html;
    }

//    public static void main(String[] args) throws Exception {
//        String url = "https://www.bing.com";
//
//        String html = get(url);
//        System.out.println(html);
//    }
}
