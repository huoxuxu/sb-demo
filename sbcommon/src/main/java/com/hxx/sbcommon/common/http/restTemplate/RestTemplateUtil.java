package com.hxx.sbcommon.common.http.restTemplate;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-21 9:41:45
 **/
public class RestTemplateUtil {
    private static SimpleClientHttpRequestFactory clientHttpRequestFactory;
    private static RestTemplate restTemplate;

    static {
        {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            // 读取超时时间
            factory.setReadTimeout(5000);
            // 连接超时时间
            factory.setConnectTimeout(5000);
            clientHttpRequestFactory = factory;
        }

        restTemplate = new RestTemplate(clientHttpRequestFactory);
    }

    /**
     * 设置代理
     *
     * @param hostName
     * @param port
     */
    public static void setProxy(String hostName, Integer port) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(hostName, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        clientHttpRequestFactory.setProxy(proxy);
    }

    /**
     * 清除代理
     */
    public static void clearProxy() {
        clientHttpRequestFactory.setProxy(null);
    }

    /**
     * GET
     *
     * @param url
     * @return
     * @throws RestClientException
     */
    public static ResponseEntity<String> get(String url) throws RestClientException {
        return restTemplate.getForEntity(url, String.class);
    }

    /**
     * POST JSON
     *
     * @param url
     * @param json
     * @param reqHeaderConsumer
     * @return
     * @throws RestClientException
     */
    public static ResponseEntity<String> postWithJson(String url, String json,
                                                      Consumer<HttpHeaders> reqHeaderConsumer) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (reqHeaderConsumer != null) {
            reqHeaderConsumer.accept(headers);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(json, headers);
        return restTemplate.postForEntity(url, httpEntity, String.class);
    }

    /**
     * POST FormUrlEncode
     *
     * @param url
     * @param kvMap
     * @param reqHeaderConsumer
     * @return
     * @throws RestClientException
     */
    public static ResponseEntity<String> postWithKV(String url, Map<String, Object> kvMap,
                                                    Consumer<HttpHeaders> reqHeaderConsumer) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (reqHeaderConsumer != null) {
            reqHeaderConsumer.accept(headers);
        }
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        if (!MapUtils.isEmpty(kvMap)) {
            for (Map.Entry<String, Object> item : kvMap.entrySet()) {
                params.add(item.getKey(), item.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(url, httpEntity, String.class);
    }

    private static ResponseEntity<String> postWithMultValue(String url, String json,
                                                            Consumer<HttpHeaders> reqHeaderConsumer) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if (reqHeaderConsumer != null) {
            reqHeaderConsumer.accept(headers);
        }

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.put("id", Collections.singletonList(request));

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(url, httpEntity, String.class);
    }
    // ----------------------------------GET-------------------------------------------------------

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType) throws RestClientException {
        return restTemplate.getForEntity(url, responseType);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * GET请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, Map<String, String> headers, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return get(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的GET请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return exchange(url, HttpMethod.GET, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------POST-------------------------------------------------------

    /**
     * 自定义POST请求调用方式
     *
     * @param url          请求URL
     * @param body         请求URL
     * @param responseType 返回对象类型
     * @return
     */
    public <T> ResponseEntity<T> post(String url, String body, Class<T> responseType) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(url, httpEntity, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @return
     */
    public <T> ResponseEntity<T> post(String url, Class<T> responseType) throws RestClientException {
        return restTemplate.postForEntity(url, HttpEntity.EMPTY, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType)
            throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * POST请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.postForEntity(url, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody,
                                      Class<T> responseType, Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                      Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, Map<String, String> headers, Object requestBody,
                                      Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return post(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的POST请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return post(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                      Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的POST请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> post(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                      Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------PUT-------------------------------------------------------

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return put(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * PUT请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Object requestBody, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, Map<String, String> headers, Object requestBody, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return put(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的PUT请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return put(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                     Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的PUT请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> put(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                     Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------DELETE-------------------------------------------------------

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Map<String, ?> uriVariables)
            throws RestClientException {
        return delete(url, HttpEntity.EMPTY, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * DELETE请求调用方式
     *
     * @param url          请求URL
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Object requestBody, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType, Object... uriVariables)
            throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody,
                                        Class<T> responseType, Object... uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, Map<String, String> headers, Object requestBody,
                                        Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        return delete(url, httpHeaders, requestBody, responseType, uriVariables);
    }

    /**
     * 带请求头的DELETE请求调用方式
     *
     * @param url          请求URL
     * @param headers      请求头参数
     * @param requestBody  请求参数体
     * @param responseType 返回对象类型
     * @param uriVariables URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, Object requestBody, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestBody, headers);
        return delete(url, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                        Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    /**
     * 自定义请求头和请求体的DELETE请求调用方式
     *
     * @param url           请求URL
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> delete(String url, HttpEntity<?> requestEntity, Class<T> responseType,
                                        Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, responseType, uriVariables);
    }

    // ----------------------------------通用方法-------------------------------------------------------

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，按顺序依次对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          Class<T> responseType, Object... uriVariables) throws RestClientException {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    /**
     * 通用调用方式
     *
     * @param url           请求URL
     * @param method        请求方法类型
     * @param requestEntity 请求头和请求体封装对象
     * @param responseType  返回对象类型
     * @param uriVariables  URL中的变量，与Map中的key对应
     * @return ResponseEntity 响应对象封装类
     */
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
                                          Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

}
