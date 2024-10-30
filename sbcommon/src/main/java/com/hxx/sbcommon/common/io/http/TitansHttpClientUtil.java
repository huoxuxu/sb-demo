package com.hxx.sbcommon.common.io.http;

import com.hxx.sbcommon.common.io.http.apachehttpclient.*;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Class: HttpClientUtils</p>
 * <p>Description:  发送Http请求Util</p>
 *
 * @author luoboran@zto.cn
 * @version 1.0
 * @date 2017/5/22.
 */
public class TitansHttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(TitansHttpClientUtil.class);

    public static final String CHAR_SET = "UTF-8";
    public static final String CHAR_SET_HEADER_KEY = "charset";
    private static final String CONTENT_VALUE_FORM = "application/x-www-form-urlencoded";
    private static final String CONTENT_VALUE_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-type";

    private static final CloseableHttpClient HTTP_CLIENT;
    public static final String HTTP_SERVICE_NAME_KEY = "httpServieName";
    private static ThreadLocal<String> httpServiceNameTL = new ThreadLocal<>();

    private static ThreadLocal<Boolean> throw404Exception = new ThreadLocal<>();
    //private static final HttpClientFilterChain httpClientFilterChain;
    private static final List<HttpClientFilter> filterList = new ArrayList<>();

    private static ThreadLocal<HttpClientFilterChain> httpClientFilterChainThreadLocal = new ThreadLocal<>();

    //默认拿连接超时时间 1 分钟
    public static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 1000 * 60;

    private static PoolingHttpClientConnectionManager cm = null;

    private TitansHttpClientUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static String getCharSet(Map<String, String> header) {
        return Optional.ofNullable(header).map(o -> o.get(CHAR_SET_HEADER_KEY)).orElse(CHAR_SET);
    }

    private static String getContentType(String contentType, String charSet) {
        return MessageFormat.format("{0};charset={1}", contentType, charSet);
    }

    public static void setHttpServiceName(String httpServiceName) {
        httpServiceNameTL.set(httpServiceName);
    }

    public static void setThrow404Exception(Boolean decode404) {
        throw404Exception.set(decode404);
    }

    public static boolean getThrow404Exception() {
        return Optional.ofNullable(throw404Exception.get()).orElse(false);
    }

    static {
        cm = new PoolingHttpClientConnectionManager();
        //连接池最大并发连接数
        cm.setMaxTotal(50);
        //单路由最大并发数
        cm.setDefaultMaxPerRoute(50);

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create()
                .setConnectionManager(cm)
                .evictIdleConnections(30, TimeUnit.SECONDS);
        Set<String> httpInterceptors = PluginConfigManager.getPropertyValueSet("com.zto.titans.soa.http.interceptor.HttpInterceptor");
        if (httpInterceptors != null && !httpInterceptors.isEmpty()) {
            for (String interceptor : httpInterceptors) {
                try {
                    HttpInterceptor httpInterceptor = (HttpInterceptor) Class.forName(interceptor).newInstance();
                    httpClientBuilder.addInterceptorFirst((HttpRequestInterceptor) httpInterceptor);
                    httpClientBuilder.addInterceptorFirst((HttpResponseInterceptor) httpInterceptor);
                    httpClientBuilder.setRetryHandler(httpInterceptor);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Http加载插件异常,请检查配置文件[{0}]", e);
                }
            }
        }
        HTTP_CLIENT = httpClientBuilder.build();
    }

    /**
     * Plugin方式加载httpClientFilter,构造FilterChain
     */
    static {
        List<PluginDecorator<Class>> orderedPluginClasses = PluginConfigManager.getOrderedPluginClasses("com.zto.titans.soa.http.filter.HttpClientFilter", true);
        if (orderedPluginClasses != null) {
            orderedPluginClasses.forEach(o -> {
                try {
                    HttpClientFilter filter = (HttpClientFilter) Class.forName(o.getPlugin().getName()).newInstance();
                    filterList.add(filter);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Http加载插件异常,请检查配置文件[com.zto.titans.soa.http.filter.HttpClientFilter]",e);
                }
            });
        }
    }

    /**
     * 获取Http 请求配置
     *
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @return RequestConfig
     */
    public static RequestConfig getRequestConfig(int connectTime, int socketConnectTime) {
        return getRequestConfig(connectTime, socketConnectTime, 1000 * 60 * 10);
    }

    /**
     * 获取Http请求配置
     *
     * @param connectTimeout           建连超时时间
     * @param socketTimeout            socket连接超时
     * @param connectionRequestTimeout 连接池中拿连接超时
     * @return
     */
    public static RequestConfig getRequestConfig(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        return RequestConfig
                .custom()
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .build();
    }

    /**
     * 发送Http-Get 请求，connectionRequestTimeout默认为1分钟
     *
     * @param url               请求URL
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param params            请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpGet(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> params) throws IOException {
        return sendHttpGet(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, params);
    }

    /**
     * 初始化http Header信息
     *
     * @param request http请求
     * @param header  请求头
     */
    public static void initHeader(HttpUriRequest request, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }


    /**
     * 执行http请求
     *
     * @param request http请求
     * @param header  请求头
     * @return 请求结果
     * @throws IOException
     */
    private static String execute(HttpUriRequest request, Map<String, String> header) throws IOException {
        if (header != null) {
            initHeader(request, header);
        }
        String body = null;
        HttpResponse response = null;
        HttpEntity entity = null;
        try {
            HttpReqAndRsp httpReqAndRsp = new HttpReqAndRsp(request, response);
            // 请求执行前置过滤器处理
            beforeExecute(httpReqAndRsp);
            response = httpReqAndRsp.getHttpResponse();
            if (response == null) {
                response = HTTP_CLIENT.execute(request);
            }

            //获取结果实体
            entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, getCharSet(header));
            } else if (getThrow404Exception() == false /*&& response.getStatusLine().getStatusCode() == 404 */) {//兼容之前的逻辑,之前默认都是不抛异常，直接反序列化；
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, getCharSet(header));
            } else {
                throw SoaHttpException.errorStatus(httpServiceNameTL.get(), request, response);
            }
            return body;
        } finally {
            EntityUtils.consume(entity);
            if (response != null && response instanceof CloseableHttpResponse) {
                ((CloseableHttpResponse) response).close();//释放链接
            }
        }
    }

    /**
     * 请求执行前置过滤器处理
     *
     * @param httpReqAndRsp http请求和响应
     */
    private static void beforeExecute(HttpReqAndRsp httpReqAndRsp) {
        //获取或者创建filterChain对象
        HttpClientFilterChain httpClientFilterChain = TitansHttpClientUtil.httpClientFilterChainThreadLocal.get();

        //如果TL中没有拿到 过滤链，但是filterList 不为空，则构建一个 httpClientFilterChain对象，放入到TL中
        if (httpClientFilterChain == null && filterList.size() > 0) {
            httpClientFilterChain = HttpClientFilterChain.build(filterList);
            TitansHttpClientUtil.httpClientFilterChainThreadLocal.set(httpClientFilterChain);
        }

        if (httpClientFilterChain != null) {
            httpClientFilterChain.reset();
            httpClientFilterChain.setAttachment(HTTP_SERVICE_NAME_KEY, httpServiceNameTL.get());
            httpClientFilterChain.doFilter(httpReqAndRsp);
        }
    }


    /**
     * 发送Http-Get 请求
     *
     * @param url                      请求URL
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param params                   请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpGet(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> params) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        String charSet = getCharSet(header);
        //装填参数
        url = urlParamJoint(url, params, charSet);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        httpGet.setHeader(CONTENT_TYPE, getContentType(CONTENT_VALUE_FORM, charSet));
        logger.debug("sendHttpGet url:{},{}" + httpServiceNameTL.get(), url);
        return execute(httpGet, header);
    }

    /**
     * 发送Http-Delete 请求， connectionRequestTimeout默认为1分钟
     *
     * @param url               请求url
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param params            请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpDelete(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> params) throws IOException {
        return sendHttpDelete(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, params);
    }

    /**
     * 发送Http-Delete 请求
     *
     * @param url                      请求url
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param params                   请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpDelete(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> params) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        String charSet = getCharSet(header);
        url = urlParamJoint(url, params, charSet);
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader(CONTENT_TYPE, getContentType(CONTENT_VALUE_FORM, charSet));
        logger.debug("sendHttpDelete url {},{}", httpServiceNameTL.get(), url);
        return execute(httpDelete, header);
    }


    /**
     * 发送Http-Put 请求, connectionRequestTimout默认为1分钟
     *
     * @param url               请求url
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param param             请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpUrlEncodedPut(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> param) throws IOException {
        return sendHttpUrlEncodedPut(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
    }

    /**
     * 发送Http-Put 请求
     *
     * @param url                      请求url
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param param                    请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpUrlEncodedPut(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> param) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);
        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        String charSet = getCharSet(header);
        httpPut.setEntity(new UrlEncodedFormEntity(nvps, charSet));
        logger.debug("sendHttpUrlEncodedPut {} url={}, param={}", httpServiceNameTL.get(), url, nvps);
        httpPut.setHeader(CONTENT_TYPE, getContentType(CONTENT_VALUE_FORM, charSet));
        return execute(httpPut, header);
    }


    /**
     * 发送Http-Post 请求，connectionRequestTimeout默认为1分钟
     *
     * @param url               请求url
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param param             请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpUrlEncodedPost(String url, int connectTime, int socketConnectTime, Map<String, String> header, Map<String, String> param) throws IOException {
        return sendHttpUrlEncodedPost(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
    }

    /**
     * 发送Http-Post 请求
     *
     * @param url                      请求url
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param param                    请求参数
     * @return
     * @throws IOException
     */
    public static String sendHttpUrlEncodedPost(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, Map<String, String> param) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        //装填参数
        List<NameValuePair> nvps = new ArrayList<>();

        if (param != null) {
            for (Map.Entry<String, String> entry : param.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        String charSet = getCharSet(header);
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, charSet));
        logger.debug("sendHttpUrlEncodedPost {} url={}, param={}", httpServiceNameTL.get(), url, nvps);
        httpPost.setHeader(CONTENT_TYPE, getContentType(CONTENT_VALUE_FORM, charSet));
        return execute(httpPost, header);

    }


    /**
     * 发送Http-Json POST请求, connectionRequestTimeout默认为1分钟
     *
     * @param url               请求url
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param param             请求参数
     * @return
     * @throws IOException
     */
    public static String sendJsonPost(String url, int connectTime, int socketConnectTime, Map<String, String> header, String param) throws IOException {
        return sendJsonPost(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
    }

    /**
     * 发送Http-Json POST请求
     *
     * @param url                      请求url
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param param                    请求参数
     * @return
     * @throws IOException
     */
    public static String sendJsonPost(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, String param) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);

        StringEntity stringEntity = new StringEntity(param, getCharSet(header));
        stringEntity.setContentEncoding(getCharSet(header));
        stringEntity.setContentType(CONTENT_VALUE_JSON);
        httpPost.setEntity(stringEntity);

        logger.debug("sendJsonPost {} url={}, param={}", httpServiceNameTL.get(), url, param);

        return execute(httpPost, header);

    }

    /**
     * 发送Http-Json Put请求, connectionRequestTimeout默认为1分钟
     *
     * @param url               请求url
     * @param connectTime       请求连接超时时间
     * @param socketConnectTime socket连接超时时间
     * @param header            请求头
     * @param param             请求参数
     * @return
     * @throws IOException
     */
    public static String sendJsonPut(String url, int connectTime, int socketConnectTime, Map<String, String> header, String param) throws IOException {
        return sendJsonPut(url, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT, header, param);
    }

    /**
     * 发送Http-Json Put请求
     *
     * @param url                      请求url
     * @param connectTime              请求连接超时时间
     * @param socketConnectTime        socket连接超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @param header                   请求头
     * @param param                    请求参数
     * @return
     * @throws IOException
     */
    public static String sendJsonPut(String url, int connectTime, int socketConnectTime, int connectionRequestTimeout, Map<String, String> header, String param) throws IOException {
        String body = null;
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        HttpPut httpPut = new HttpPut(url);
        httpPut.setConfig(requestConfig);

        StringEntity stringEntity = new StringEntity(param, getCharSet(header));
        stringEntity.setContentEncoding(getCharSet(header));
        stringEntity.setContentType(CONTENT_VALUE_JSON);
        httpPut.setEntity(stringEntity);
        logger.debug("sendJsonPut {} url={}, param={}", httpServiceNameTL.get(), url, param);
        return execute(httpPut, header);
    }


    /**
     * url参数拼接
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     * @throws IOException
     */
    public static String urlParamJoint(String url, Map<String, String> params) throws IOException {
        return urlParamJoint(url, params, CHAR_SET);
    }

    /**
     * url参数拼接
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     * @throws IOException
     */
    public static String urlParamJoint(String url, Map<String, String> params, String charset) throws IOException {

        //装填参数
        if (params != null && !params.isEmpty()) {
            List<NameValuePair> pairs = new ArrayList<>(
                    params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
            url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
        }
        return url;
    }


    public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create("text/plain", Consts.UTF_8);

    /**
     * 上传文件， connectionRequestTimeout默认为1分钟
     *
     * @param url               上传地址
     * @param param             上传参数
     * @param name              上传名称
     * @param file              上传文件体
     * @param connectTime       建连超时时间
     * @param socketConnectTime socket超时时间
     * @return
     * @throws IOException
     */
    public static String sendFileUpload(String url, Map<String, String> param, String name, Object file, int connectTime, int socketConnectTime) throws IOException {
        return sendFileUpload(url, param, name, file, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
    }

    /**
     * 上传文件
     *
     * @param url                      上传地址
     * @param param                    上传参数
     * @param name                     上传名称
     * @param file                     上传文件体,支持byte[]和InputStream
     * @param connectTime              建连超时时间
     * @param socketConnectTime        socket超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @return
     * @throws IOException
     */
    public static String sendFileUpload(String url, Map<String, String> param, String name, Object file, int connectTime, int socketConnectTime, int connectionRequestTimeout) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        httpPost.setConfig(requestConfig);
        //创建待上传的文件
        MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();

        mEntityBuilder.setCharset(Charset.forName("UTF-8"));//设置请求的编码格式
        mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

        String filename = param.get("filename");
        if (filename != null && !filename.contains(".")) {
            String ext = param.get("ext");
            if (ext != null) {
                filename = filename + "." + ext;
            }
        }

        if (file instanceof byte[]) {
            mEntityBuilder.addBinaryBody(name, (byte[]) file, ContentType.MULTIPART_FORM_DATA, filename);
        } else if (file instanceof InputStream) {
            mEntityBuilder.addBinaryBody(name, (InputStream) file, ContentType.MULTIPART_FORM_DATA, filename);
        }

        //对请求的表单域进行填充
        param.forEach((key, value) -> mEntityBuilder.addTextBody(key, value, TEXT_PLAIN_UTF_8));

        httpPost.setEntity(mEntityBuilder.build());
        logger.debug("sendFileUpload {} url={}, param={}", httpServiceNameTL.get(), url, param);
        return execute(httpPost, null);
    }

    /**
     * 下载文件， connectionRequestTimeout默认为1分钟
     *
     * @param url               下载地址
     * @param param             参数
     * @param connectTime       建连超时时间
     * @param socketConnectTime socket超时时间
     * @return
     * @throws IOException
     */
    public static byte[] sendFileDownload(String url, Map<String, String> param, int connectTime, int socketConnectTime) throws IOException {
        return sendFileDownload(url, param, connectTime, socketConnectTime, DEFAULT_CONNECTION_REQUEST_TIMEOUT);
    }

    /**
     * 下载文件
     *
     * @param url                      下载地址
     * @param param                    参数
     * @param connectTime              建连超时时间
     * @param socketConnectTime        socket超时时间
     * @param connectionRequestTimeout 连接池拿连接超时时间
     * @return
     * @throws IOException
     */
    public static byte[] sendFileDownload(String url, Map<String, String> param, int connectTime, int socketConnectTime, int connectionRequestTimeout) throws IOException {
        RequestConfig requestConfig = getRequestConfig(connectTime, socketConnectTime, connectionRequestTimeout);
        url = urlParamJoint(url, param, getCharSet(null));
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        //发送文件下载请求
        CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();

        try {
            if (httpEntity != null) {
                InputStream inputStream = httpEntity.getContent();
                return inputStreamToByte(inputStream);
            }
        } finally {
            EntityUtils.consume(httpEntity);
            response.close();
        }
        logger.warn("url={}文件下载失败，响应httpEntity为null", url);
        return null;
    }


    private static byte[] inputStreamToByte(InputStream inputStream) throws IOException {

        if (inputStream != null) {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            //buff用于存放循环读取的临时数据
            byte[] buff = new byte[1024];
            int len = -1;
            try {
                while ((len = inputStream.read(buff, 0, buff.length)) != -1) {
                    swapStream.write(buff, 0, len);
                    swapStream.flush();
                }
                return swapStream.toByteArray();
            } finally {
                swapStream.close();
                inputStream.close();
            }
        } else {
            return null;
        }
    }
}
