package com.hxx.sbConsole.service.impl.http;

import com.alibaba.fastjson.JSONObject;
import com.hxx.sbConsole.model.HttpTestDemoModel;
import com.hxx.sbcommon.common.io.http.ApacheHttpClientUseful;
import com.hxx.sbcommon.common.io.http.client.HttpApiClient;
import com.hxx.sbcommon.common.io.json.FastJsonReaderQuick;
import com.hxx.sbcommon.common.io.json.JsonUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-05 12:59:30
 **/
public class HttpClientServiceDemo {
    static String url = "http://127.0.0.1:666/HttpTest/Index?k=k1";
    static Map<String, String> headers = new HashMap<>();

    public static void main(String[] args) throws Exception {
        headers.put("a", "a1");
        headers.put("b", "b1");

        try {
            // get
            case1();
            // post
            case2();
            // upload
            case3();
            // Client
            case4();

            for (int i = 0; i < 10; i++) {

            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("ok!");
    }

    // get
    static void case1() throws IOException {
        String apiName = "Test-Http-Get";
        String html = HttpApiClient.get(apiName, url, headers);
        System.out.println("=====================================================");
        System.out.println(html);
    }

    // post
    static void case2() throws IOException {
        {
            String apiName = "Test-Http-PostJson";
            String html = HttpApiClient.postJson(apiName, url, headers, "[{\"id\":\"哈喽\"}]");
            System.out.println("=====================================================");
            System.out.println(html);
        }

        {
            String apiName = "Test-Http-PostUrlEncoded";
            String para = "code=1&name=kt";
            String html = HttpApiClient.postUrlEncoded(apiName, url, headers, para);
            System.out.println("=====================================================");
            System.out.println(html);
        }
    }

    static void case3() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("pa", "pa1");
        params.put("pb", "pb1");
        File file = new File("d:/tmp/tmp1.txt");
        File file2 = new File("d:/tmp/tmp1.txt");

        List<ApacheHttpClientUseful.MultipartFormDataInfo> uploadFiles = new ArrayList<>();
        uploadFiles.add(new ApacheHttpClientUseful.MultipartFormDataInfo("file", file));
        uploadFiles.add(new ApacheHttpClientUseful.MultipartFormDataInfo("file2", file2));
        // send http
        String html = ApacheHttpClientUseful.sendFileUpload(url, headers, params, uploadFiles);
        System.out.println("=====================================================");
        System.out.println(html);
    }

    static void case4() throws IOException {
        try {
            String txt = BaiduHttpApiClient.getContent();
            System.out.println(txt);
        } catch (Exception e) {
            System.out.println(e + "");
        }
        try {
            String txt = BaiduHttpApiClient.getContent2();
            System.out.println(txt);
        } catch (Exception e) {
            System.out.println(e + "");
        }
    }

    // 解析Reader
    private static void parseReader(Reader reader) {
        try (FastJsonReaderQuick jsu = new FastJsonReaderQuick(reader)) {
            HttpTestDemoModel model = jsu.parsePOJO((ind, modelMap) -> {
                HttpTestDemoModel m = new HttpTestDemoModel();
                for (Map.Entry<String, Object> entry : modelMap.entrySet()) {
                    String k = entry.getKey();
                    Object v = entry.getValue();

                    switch (k) {
                        case "ok":
                            if (v instanceof Integer) {
                                m.setOk((Integer) v);
                            }
                            break;
                        case "msg":
                            if (v instanceof String) {
                                m.setMsg((String) v);
                            }
                            break;
                        case "data":
                            if (v instanceof JSONObject) {
                                HttpTestDemoModel.DataDemoModel ddm = new HttpTestDemoModel.DataDemoModel();
                                for (Map.Entry<String, Object> vItem : ((JSONObject) v).entrySet()) {
                                    String vk = vItem.getKey();
                                    Object vv = vItem.getValue();
                                    switch (vk) {
                                        case "Method":
                                            if (vv instanceof String) {
                                                ddm.setMethod((String) vv);
                                            }
                                            break;
                                        case "Path":
                                            if (vv instanceof String) {
                                                ddm.setPath((String) vv);
                                            }
                                            break;
                                        case "Query":
                                            if (vv instanceof String) {
                                                ddm.setQuery((String) vv);
                                            }
                                            break;
                                        case "Header":
                                            if (vv instanceof String) {
                                                ddm.setHeader((String) vv);
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                m.setData(ddm);
                            }
                            break;
                        default:
                            break;
                    }
                }
                return m;
            });

            System.out.println("model:" + JsonUtil.toJSON(model));
        }
    }

}
