package com.hxx.sbcommon.common.io.http;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
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

import javax.net.ssl.SSLContext;
import java.io.*;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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
    public static final String CHAR_SET = "UTF-8";

    // 指定与远程主机建立连接的超时时间
    public static final Integer DEFAULT_CONNECTION_TIMEOUT = 10000;
    // 指定建立连接后等待数据的超时时间
    public static final Integer DEFAULT_SOCKET_TIMEOUT = 10000;
    // 指定从连接池获取连接的超时时间
    public static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000;

    // Content-Type: text/plain; charset=UTF-8
    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);

    private static final CloseableHttpClient HTTP_CLIENT;


    /**
     * 上传文件
     *
     * @param url
     * @param header
     * @param param
     * @param name
     * @param files
     * @return
     * @throws IOException
     */
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

            try (ApacheHttpClientSimpleUseful.HttpApiResp resp = ApacheHttpClientSimpleUseful.HttpClientTools.execute(httpPost, header)) {
                log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                if (!resp.isOk()) {
                    log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                    return null;
                }

                return ApacheHttpClientSimpleUseful.HttpClientTools.processResp(resp);
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

    /**
     * 上传文件
     *
     * @param url
     * @param header
     * @param param
     * @param name
     * @param fileNames
     * @param files
     * @return
     * @throws IOException
     */
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

        try (ApacheHttpClientSimpleUseful.HttpApiResp resp = ApacheHttpClientSimpleUseful.HttpClientTools.execute(httpPost, header)) {
            log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.isOk()) {
                log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return ApacheHttpClientSimpleUseful.HttpClientTools.processResp(resp);
        }
    }

    /**
     * 上传文件
     *
     * @param url
     * @param header
     * @param param
     * @param name
     * @param fileNames
     * @param files
     * @return
     * @throws IOException
     */
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

        try (ApacheHttpClientSimpleUseful.HttpApiResp resp = ApacheHttpClientSimpleUseful.HttpClientTools.execute(httpPost, header)) {
            log.debug("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
            if (!resp.isOk()) {
                log.warn("sendFileUpload-resp url:{} statusCode:{} contentLength:{}", url, resp.getStatusCode(), resp.getContentLength());
                return null;
            }

            return ApacheHttpClientSimpleUseful.HttpClientTools.processResp(resp);
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

    static {
        HTTP_CLIENT = ApacheHttpClientSimpleUseful.getHttpClient();
    }

}
