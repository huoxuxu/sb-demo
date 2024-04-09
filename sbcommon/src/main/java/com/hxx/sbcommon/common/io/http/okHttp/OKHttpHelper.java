package com.hxx.sbcommon.common.io.http.okHttp;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class OKHttpHelper {
    static OkHttpClient okHttpClient;

    static {
        initOkHttpClient();
    }

    //初始化okHttpClient
    private static void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);
        okHttpClient = builder.build();
    }

    public static String GetString(String url) throws Exception {
        final Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception(response.code() + "err " + response.body().string());
        }

        return response.body().string();
    }

    public static String PostFormData(String url, Map<String, String> map) throws Exception {
        RequestBody requestBody = null;
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        if (map != null && !map.isEmpty()) {

            //id=1&name=2
            List<String> ls = new ArrayList<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String a = entry.getKey() + "=" + entry.getValue();
                ls.add(a);
            }
            String lsStr = StringUtils.join(ls, "&");
            requestBody = RequestBody.create(mediaType, lsStr);
        } else {
            requestBody = RequestBody.create(mediaType, "");
        }

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //同步方法
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception(response.code() + "err " + response.body().string());
        }

        return response.body().string();
    }

    public static String PostJson(String url, String json, Map<String, String> reqHeader) throws Exception {
        RequestBody requestBody = null;
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        requestBody = RequestBody.create(mediaType, json);

        Request.Builder build = new Request.Builder()
                .url(url)
                .post(requestBody);
        if (reqHeader != null && !reqHeader.isEmpty()) {
            for (Map.Entry<String, String> entry : reqHeader.entrySet()) {
                build.addHeader(entry.getKey(), entry.getValue());
            }
        }
        final Request request = build.build();
        //同步方法
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception(response.code() + "err " + response.body().string());
        }

        return response.body().string();
    }

    //上传文件，和文本 异步
    public static void sendPhotoRequestAsync(String url, String openid, String fileName) throws IOException, InterruptedException {
        MediaType mediaType = MediaType.parse("img/jpg");
        File file = new File(fileName);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileimage", file.getName(), RequestBody.create(mediaType, file))
                .addFormDataPart("openid", openid)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //异步方法
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("err");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
                //强制关闭该线程，如果没有这行代码，会阻塞，即使接收到相应程序还不会终止
                okHttpClient.dispatcher().executorService().shutdown();
            }
        });
        //同步方法
//        Response response=okHttpClient.newCall(request).execute();
//        if(response.isSuccessful()){
//            System.out.println(response.body().string());
//        }else{
//          System.err.println("err"+response.body().string());
//        }
    }

    //上传文件，和文本 同步
    public static String Post(String url, String openid, String fileName) throws Exception {
        MediaType mediaType = MediaType.parse("img/jpg");
        File file = new File(fileName);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileimage", file.getName(), RequestBody.create(mediaType, file))
                .addFormDataPart("openid", openid)
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        //同步方法
        Response response = okHttpClient.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new Exception("err" + response.body().string());
        }
        return response.body().string();
    }


}
