package com.hxx.sbConsole.external.okHttpClient;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-14 14:28:00
 **/
@Slf4j
public class OkHttpClientHelper {
    private static OkHttpClient okHttpClient;

    static {
        init();
    }

    static void init() {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(8000, TimeUnit.MILLISECONDS)
                .readTimeout(8000, TimeUnit.MILLISECONDS)
                .build();
    }

    public String get(String url) throws Exception {
        return get(url, null);
    }

    public String get(String url, Map<String, String> headerMap) throws Exception {
        Headers headers = setHeaders(headerMap);
        Request request = new Request.Builder().url(url)
                .headers(headers)
                .build();

        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            ensureSuccessCode(response);

            try (ResponseBody respBody = response.body()) {
                OftenUtil.assertCond(respBody == null, "响应体不能为空");
                String strResponse = respBody.string();
                return strResponse;
            }
        }
    }

    public String post(String url, Map<String, String> postBody) throws Exception {
        return post(url, postBody, null);
    }

    public String post(String url, Map<String, String> postBody, Map<String, String> headerMap) throws Exception {
        Headers headers = setHeaders(headerMap);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (postBody != null && postBody.size() > 0) {
            for (Map.Entry<String, String> entry : postBody.entrySet()) {
                bodyBuilder = bodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        RequestBody reqBody = bodyBuilder.build();
        Request request = new Request.Builder().url(url)
                .headers(headers)
                .post(reqBody)
                .build();

        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            ensureSuccessCode(response);

            try (ResponseBody respBody = response.body()) {
                String strResponse = respBody.string();
                return strResponse;
            }
        }
    }

    public String postJson(String url, String json) throws Exception {
        return postJson(url, json, null);
    }

    public String postJson(String url, String json, Map<String, String> headerMap) throws Exception {
        Headers headers = setHeaders(headerMap);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody reqBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder().url(url)
                .headers(headers)
                .post(reqBody)
                .build();

        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            ensureSuccessCode(response);

            try (ResponseBody respBody = response.body()) {
                String strResponse = respBody.string();
                return strResponse;
            }
        }
    }

    public String uploadFile(String url, String formKeyName, String fileName, File file, Map<String, String> headerMap) throws Exception {
        Headers headers = setHeaders(headerMap);
        RequestBody reqBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(formKeyName, fileName, RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request request = new Request.Builder().url(url)
                .headers(headers)
                .post(reqBody)
                .build();

        Call call = okHttpClient.newCall(request);
        try (Response response = call.execute()) {
            ensureSuccessCode(response);

            try (ResponseBody respBody = response.body()) {
                String strResponse = respBody.string();
                return strResponse;
            }
        }
    }


    // 确保响应Code为200
    private void ensureSuccessCode(Response response) throws Exception {
        int code = response.code();
        if (code != 200) {
            throw new Exception("请求失败，HttpStatusCode：" + code);
        }
    }

    // 设置请求头
    public static Headers setHeaders(Map<String, String> headersMap) {
        okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
        if (headersMap != null && headersMap.size() > 0) {
            headersMap.forEach(headersbuilder::add);
        }
        Headers headers = headersbuilder.build();
        return headers;
    }

//    private static ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
//    //单例模式返回一个实例
//    public static OkHttpClient getInstance() {
//        if (client == null)
//            synchronized (OkHttpClient.class) {
//                if (client == null) {
////添加cookieJar,自动化管理cookie,获得一致的sessions值
////添加连接超时和读取超时,在网络状况不好的时候可以做出提示.
//                    client = new OkHttpClient.Builder().cookieJar(new CookieJar() {
//                                @Override
//                                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                                    Log.i(TAG, "saveFromResponse: cookies=" + cookies);
//                                    Log.i(TAG, "saveFromResponse: cookieStore=" + cookieStore.size());
//                                    cookieStore.put(url.host(), cookies);
//                                }
//
//                                @Override
//                                public List<Cookie> loadForRequest(HttpUrl url) {
//                                    List<Cookie> cookies = cookieStore.get(url.host());
//                                    Log.i(TAG, "loadForRequest: cookies=" + cookies);
//                                    return cookies != null ? cookies : new ArrayList<Cookie>();
//                                }
//                            })
//                            .connectTimeout(8000, TimeUnit.MILLISECONDS)
//                            .readTimeout(8000, TimeUnit.MILLISECONDS)
//                            .build();
//                }
//            }
//        return client;
//    }
}
