//package com.hxx.sbcommon.common.http;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.Charset;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.http.Consts;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpDelete;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.client.methods.HttpUriRequest;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class HttpClientUtils {
//    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
//    private static final CloseableHttpClient HTTP_CLIENT;
//    public static final String CHAR_SET = "UTF-8";
//    public static final String CHAR_SET_HEADER_KEY = "charset";
//    private static ThreadLocal<String> httpServiceNameTL = new ThreadLocal();
//    private static ThreadLocal<Boolean> throw404Exception = new ThreadLocal();
//    public static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000;
//    private static PoolingHttpClientConnectionManager cm = null;
//    private static final String CONTENT_VALUE_FORM = "application/x-www-form-urlencoded";
//    private static final String CONTENT_VALUE_JSON = "application/json";
//    private static final String CONTENT_TYPE = "Content-type";
//    public static final ContentType TEXT_PLAIN_UTF_8;
//
//    private HttpClientUtils() {
//        throw new IllegalStateException("Utility class");
//    }
//
//    private static String getCharSet(Map<String, String> header) {
//        return (String) Optional.ofNullable(header).map((o) -> {
//            return (String) o.get("charset");
//        }).orElse("UTF-8");
//    }
//
//    private static String getContentType(String contentType, String charSet) {
//        return MessageFormat.format("{0};charset={1}", contentType, charSet);
//    }
//
//    public static void setHttpServiceName(String httpServiceName) {
//        httpServiceNameTL.set(httpServiceName);
//    }
//
//    public static void setThrow404Exception(Boolean decode404) {
//        throw404Exception.set(decode404);
//    }
//
//    public static boolean getThrow404Exception() {
//        return (Boolean) Optional.ofNullable(throw404Exception.get()).orElse(false);
//    }
//
//    public static RequestConfig getRequestConfig(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
//        return RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).build();
//    }
//
//    public static void initHeader(HttpUriRequest request, Map<String, String> header) {
//        if (header == null) return;
//
//        for (Map.Entry<String, String> entry : header.entrySet()) {
//            request.setHeader((String) entry.getKey(), (String) entry.getValue());
//        }
//    }
//
//    private static String execute(HttpUriRequest request, Map<String, String> header) throws IOException {
//        if (header != null) {
//            initHeader(request, header);
//        }
//
//        String body = null;
//        HttpResponse response = null;
//        HttpEntity entity = null;
//
//        String var6;
//        try {
//            response = HTTP_CLIENT.execute(request);
//
//            entity = ((HttpResponse) response).getEntity();
//            if (((HttpResponse) response).getStatusLine().getStatusCode() >= 200 && ((HttpResponse) response).getStatusLine().getStatusCode() < 300) {
//                body = EntityUtils.toString(entity, getCharSet(header));
//            } else {
//                if (getThrow404Exception()) {
//                    throw SoaHttpException.errorStatus((String) httpServiceNameTL.get(), request, (HttpResponse) response);
//                }
//
//                body = EntityUtils.toString(entity, getCharSet(header));
//            }
//
//            var6 = body;
//        } finally {
//            EntityUtils.consume(entity);
//            if (response != null && response instanceof CloseableHttpResponse) {
//                ((CloseableHttpResponse) response).close();
//            }
//
//        }
//
//        return var6;
//    }
//
//    public static String sendHttpGet(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> params) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        String charSet = getCharSet(header);
//        url = urlParamJoint(url, params, charSet);
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(requestConfig);
//        httpGet.setHeader("Content-type", getContentType("application/x-www-form-urlencoded", charSet));
//        logger.debug("sendHttpGet url:{},{}" + (String) httpServiceNameTL.get(), url);
//        return execute(httpGet, header);
//    }
//
//    public static String sendHttpDelete(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> params) throws IOException {
//        return sendHttpDelete(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, params);
//    }
//
//    public static String sendHttpDelete(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> params) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        String charSet = getCharSet(header);
//        url = urlParamJoint(url, params, charSet);
//        HttpDelete httpDelete = new HttpDelete(url);
//        httpDelete.setConfig(requestConfig);
//        httpDelete.setHeader("Content-type", getContentType("application/x-www-form-urlencoded", charSet));
//        logger.debug("sendHttpDelete url {},{}", httpServiceNameTL.get(), url);
//        return execute(httpDelete, header);
//    }
//
//    public static String sendHttpUrlEncodedPut(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> param) throws IOException {
//        return sendHttpUrlEncodedPut(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
//    }
//
//    public static String sendHttpUrlEncodedPut(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> param) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        HttpPut httpPut = new HttpPut(url);
//        httpPut.setConfig(requestConfig);
//        List<NameValuePair> nvps = new ArrayList();
//        if (param != null) {
//            Iterator var10 = param.entrySet().iterator();
//
//            while (var10.hasNext()) {
//                Map.Entry<String, String> entry = (Map.Entry) var10.next();
//                nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
//            }
//        }
//
//        String charSet = getCharSet(header);
//        httpPut.setEntity(new UrlEncodedFormEntity(nvps, charSet));
//        logger.debug("sendHttpUrlEncodedPut {} url={}, param={}", new Object[]{httpServiceNameTL.get(), url, nvps});
//        httpPut.setHeader("Content-type", getContentType("application/x-www-form-urlencoded", charSet));
//        return execute(httpPut, header);
//    }
//
//    public static String sendHttpUrlEncodedPost(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> param) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
//        List<NameValuePair> nvps = new ArrayList();
//        if (param != null) {
//            Iterator var10 = param.entrySet().iterator();
//
//            while (var10.hasNext()) {
//                Map.Entry<String, String> entry = (Map.Entry) var10.next();
//                nvps.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
//            }
//        }
//
//        String charSet = getCharSet(header);
//        httpPost.setEntity(new UrlEncodedFormEntity(nvps, charSet));
//        logger.debug("sendHttpUrlEncodedPost {} url={}, param={}", new Object[]{httpServiceNameTL.get(), url, nvps});
//        httpPost.setHeader("Content-type", getContentType("application/x-www-form-urlencoded", charSet));
//        return execute(httpPost, header);
//    }
//
//    public static String sendJsonPost(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, String param) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setConfig(requestConfig);
//        StringEntity stringEntity = new StringEntity(param, getCharSet(header));
//        stringEntity.setContentEncoding(getCharSet(header));
//        stringEntity.setContentType("application/json");
//        httpPost.setEntity(stringEntity);
//        logger.debug("sendJsonPost {} url={}, param={}", new Object[]{httpServiceNameTL.get(), url, param});
//        return execute(httpPost, header);
//    }
//
//    public static String sendJsonPut(String url, int connectTime, int socketConnectTime, Map<String, String> header, String param) throws IOException {
//        return sendJsonPut(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
//    }
//
//    public static String sendJsonPut(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, String param) throws IOException {
//        String body = null;
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        HttpPut httpPut = new HttpPut(url);
//        httpPut.setConfig(requestConfig);
//        StringEntity stringEntity = new StringEntity(param, getCharSet(header));
//        stringEntity.setContentEncoding(getCharSet(header));
//        stringEntity.setContentType("application/json");
//        httpPut.setEntity(stringEntity);
//        logger.debug("sendJsonPut {} url={}, param={}", new Object[]{httpServiceNameTL.get(), url, param});
//        return execute(httpPut, header);
//    }
//
//    public static String urlParamJoint(String url, Map<String, String> params) throws IOException {
//        return urlParamJoint(url, params, "UTF-8");
//    }
//
//    public static String urlParamJoint(String url, Map<String, String> params, String charset) throws IOException {
//        if (params != null && !params.isEmpty()) {
//            List<NameValuePair> pairs = new ArrayList(params.size());
//            Iterator var4 = params.entrySet().iterator();
//
//            while (var4.hasNext()) {
//                Map.Entry<String, String> entry = (Map.Entry) var4.next();
//                String value = (String) entry.getValue();
//                if (value != null) {
//                    pairs.add(new BasicNameValuePair((String) entry.getKey(), value));
//                }
//            }
//
//            url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
//        }
//
//        return url;
//    }
//
//    public static String sendFileUpload(String url, Map<String, String> param, String name, Object file, int connectTime, int socketConnectTime) throws IOException {
//        return sendFileUpload(url, param, name, file, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
//    }
//
//    public static String sendFileUpload(String url, Map<String, String> param, String name, Object file, int connectTime, int socketConnectTime, int connectionRequestTimeout) throws IOException {
//        HttpPost httpPost = new HttpPost(url);
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        httpPost.setConfig(requestConfig);
//        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
//        mEntityBuilder.setCharset(Charset.forName("UTF-8"));
//        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//        String filename = (String) param.get("filename");
//        if (filename != null && !filename.contains(".")) {
//            String ext = (String) param.get("ext");
//            if (ext != null) {
//                filename = filename + "." + ext;
//            }
//        }
//
//        if (file instanceof byte[]) {
//            mEntityBuilder.addBinaryBody(name, (byte[]) ((byte[]) file), ContentType.MULTIPART_FORM_DATA, filename);
//        } else if (file instanceof InputStream) {
//            mEntityBuilder.addBinaryBody(name, (InputStream) file, ContentType.MULTIPART_FORM_DATA, filename);
//        }
//
//        param.forEach((key, value) -> {
//            mEntityBuilder.addTextBody(key, value, TEXT_PLAIN_UTF_8);
//        });
//        httpPost.setEntity(mEntityBuilder.build());
//        logger.debug("sendFileUpload {} url={}, param={}", new Object[]{httpServiceNameTL.get(), url, param});
//        return execute(httpPost, (Map) null);
//    }
//
//    public static byte[] sendFileDownload(String url, Map<String, String> param, int connectTime, int socketConnectTime) throws IOException {
//        return sendFileDownload(url, param, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
//    }
//
//    public static byte[] sendFileDownload(String url, Map<String, String> param, int connectTime, int socketConnectTime, int connectionRequestTimeout) throws IOException {
//        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
//        url = urlParamJoint(url, param, getCharSet((Map) null));
//        HttpGet httpGet = new HttpGet(url);
//        httpGet.setConfig(requestConfig);
//        CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
//        HttpEntity httpEntity = response.getEntity();
//
//        try {
//            if (httpEntity != null) {
//                InputStream inputStream = httpEntity.getContent();
//                byte[] var10 = inputStreamToByte(inputStream);
//                return var10;
//            }
//        } finally {
//            EntityUtils.consume(httpEntity);
//            response.close();
//        }
//
//        logger.warn("url={}文件下载失败，响应httpEntity为null", url);
//        return null;
//    }
//
//    private static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
//        if (inputStream == null) {
//            return null;
//        } else {
//            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
//            byte[] buff = new byte[1024];
//
//            byte[] var4;
//            try {
//                int len;
//                while ((len = inputStream.read(buff, 0, buff.length)) != -1) {
//                    swapStream.write(buff, 0, len);
//                    swapStream.flush();
//                }
//
//                var4 = swapStream.toByteArray();
//            } finally {
//                swapStream.close();
//                inputStream.close();
//            }
//
//            return var4;
//        }
//    }
//
//    static {
//        cm = new PoolingHttpClientConnectionManager();
//        cm.setMaxTotal(HttpConfigPropertiesProvider.getHttpConfigProperties().getMaxConnectionSize());
//        cm.setDefaultMaxPerRoute(HttpConfigPropertiesProvider.getHttpConfigProperties().getMaxPerRouteSize());
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setConnectionManager(cm).evictIdleConnections(HttpConfigPropertiesProvider.getHttpConfigProperties().getMaxIdleSecond(), TimeUnit.SECONDS);
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
//
//        HTTP_CLIENT = httpClientBuilder.build();
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
//
//        TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);
//    }
//}
