package com.hxx.sbcommon.common.http;


import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.encrypt.RsaUtils;
import com.hxx.sbcommon.model.HttpResEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CopyOfHttpUtils {

    private static Logger logger = LoggerFactory.getLogger(CopyOfHttpUtils.class);

    private static final CloseableHttpClient httpClient;
    // 设置超时时间
    public static final int REQUEST_TIMEOUT = 2000;
    public static final int REQUEST_SOCKET_TIME = 2000;

    static {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory>create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
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
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        //设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        connManager.setMaxTotal(6000);
        connManager.setDefaultMaxPerRoute(1000);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(REQUEST_SOCKET_TIME).build();
        httpClient = HttpClientBuilder.create().setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig).build();
    }

    public static HttpResEntity doPost(String url, Map<String, String> params, String charset) {
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Entry<String, String> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(),
                            (String) value));
                }
            }
        }
        StringEntity httpEntity = null;
        try {
            httpEntity = new UrlEncodedFormEntity(pairs, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return doPost(url, httpEntity, charset);
    }

    public static HttpResEntity doPost(String url, String params, String charset, String contentType, boolean isMap) {
        StringEntity entity = null;
        Map<String, String> map = JsonUtil.parseNotThrowException(params, Map.class);

        return doPost(url, map, charset);
    }


    public static HttpResEntity doPost(String url, String params, String charset) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(params, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doPost(url, entity, charset);
    }

    public static HttpResEntity doPost(String url, String params,
                                       String charset, String contentType) {
        StringEntity entity = null;
        try {
            entity = new StringEntity(params, charset);
            entity.setContentType(contentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doPost(url, entity, charset);
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static HttpResEntity doPost(String url, StringEntity params, String charset) {
        HttpResEntity entity = new HttpResEntity();
        if (StringUtils.isBlank(url)) {
            entity.setMsg("请求地址异常");
            entity.setStatus(404);
            return entity;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                httpPost.setEntity(params);
            }
            httpPost.addHeader("version", "1.0.0.0");
            httpPost.addHeader("siteId", "2743");
            httpPost.addHeader("sign", RsaUtils.encrypt("2743"));
            httpPost.addHeader("Connection", "close");
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                entity.setData("请求异常");
                entity.setStatus(statusCode);
                return entity;
            }
            HttpEntity httpEntity = response.getEntity();
            String result = null;
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, charset);
            }
            EntityUtils.consume(httpEntity);
            response.close();
            entity.setStatus(statusCode);
            entity.setData(result);
            return entity;
        } catch (SocketTimeoutException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (ConnectTimeoutException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (HttpHostConnectException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (SocketException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (Exception e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        }
    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static HttpResEntity post(String url, StringEntity params,
                                     String charset) {
        HttpResEntity entity = new HttpResEntity();
        if (StringUtils.isBlank(url)) {
            entity.setMsg("请求地址异常");
            entity.setStatus(404);
            return entity;
        }
        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                httpPost.setEntity(params);
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                entity.setData("请求异常");
                entity.setStatus(statusCode);
                return entity;
            }
            HttpEntity httpEntity = response.getEntity();
            String result = null;
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, charset);
            }
            EntityUtils.consume(httpEntity);
            response.close();
            entity.setStatus(statusCode);
            entity.setData(result);
            return entity;
        } catch (SocketTimeoutException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (ConnectTimeoutException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (HttpHostConnectException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (SocketException e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (Exception e) {
            logger.info("请求超时:{},{}", url, e);
            return null;
        }
    }

    public static HttpResEntity post(String url, Map<String, String> params,
                                     String charset) {
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Entry<String, String> entry : params.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(),
                            (String) value));
                }
            }
        }
        StringEntity httpEntity = null;
        try {
            httpEntity = new UrlEncodedFormEntity(pairs, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return post(url, httpEntity, charset);
    }

    public static HttpResEntity post(String url, InputStream is) {

        HttpResEntity entity = new HttpResEntity();
        if (StringUtils.isBlank(url)) {
            entity.setMsg("请求地址异常");
            entity.setStatus(404);
            return entity;
        }

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            if (is != null) {
                httpPost.setEntity(new InputStreamEntity(is));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                entity.setData("请求异常:statusCode" + statusCode);
                entity.setStatus(statusCode);
                return entity;
            }
            HttpEntity httpEntity = response.getEntity();
            String result = null;
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
            }
            EntityUtils.consume(httpEntity);
            response.close();
            entity.setStatus(statusCode);
            entity.setData(result);
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送POST 请求
     *
     * @param url     请求地址
     * @param charset 编码格式
     * @param params  请求参数
     * @return 响应
     * @throws IOException
     */
    public static String post(String url, String charset,
                              Map<String, String> params, String sign) throws IOException {

        HttpURLConnection conn = null;
        OutputStreamWriter out = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer result = new StringBuffer();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", charset);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            out = new OutputStreamWriter(conn.getOutputStream(), charset);
            String data = buildQuery(params, charset);
            data += "&sign=" + sign;
            out.write(data);
            out.flush();
            inputStream = conn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            String tempLine;
            while ((tempLine = reader.readLine()) != null) {
                result.append(tempLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.info("请求超时:{},{}", url, e);
            return null;
        } catch (Exception e) {
            logger.info("请求超时:{},{}", url, e);
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return result.toString();
    }

    /**
     * 将map转换为请求字符串
     * <p>
     * data=xxx&msg_type=xxx
     * </p>
     *
     * @param params
     * @param charset
     * @return
     * @throws IOException
     */
    public static String buildQuery(Map<String, String> params, String charset)
            throws IOException {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuffer data = new StringBuffer();
        boolean flag = false;

        for (Entry<String, String> entry : params.entrySet()) {
            if (flag) {
                data.append("&");
            } else {
                flag = true;
            }

            data.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue() == null ? ""
                            : entry.getValue(), charset));
        }

        return data.toString();

    }
}
