package com.hxx.sbConsole.service.impl.http;

import com.alibaba.fastjson.JSONObject;
import com.hxx.sbConsole.model.HttpTestDemoModel;
import com.hxx.sbcommon.common.io.http.ApacheHttpClientSimpleUseful;
import com.hxx.sbcommon.common.io.http.ApacheHttpClientUseful;
import com.hxx.sbcommon.common.json.FastJsonReaderQuick;
import com.hxx.sbcommon.common.json.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
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

        // get
        case1();
        // post
        case2();
        // upload
        case3();

        for (int i = 0; i < 10; i++) {

        }
        System.out.println("=====================================================");

    }

    // get
    static void case1() throws IOException {
        String html = ApacheHttpClientSimpleUseful.sendGet(url, headers, null);
        System.out.println("=====================================================");
        System.out.println(html);
    }

    // post
    static void case2() throws IOException {
        {
            String html = ApacheHttpClientSimpleUseful.sendPostJson(url, headers, "[{\"id\":1}]");
            System.out.println("=====================================================");
            System.out.println(html);
        }

        {
            Map<String, String> paramDic = new HashMap<>();
            paramDic.put("code", "1");
            paramDic.put("name", "kt");
            String html = ApacheHttpClientSimpleUseful.sendPostUrlEncoded(url, headers, paramDic);
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
        String html = ApacheHttpClientUseful.sendFileUpload(url, headers, params, "file", file, file2);
        System.out.println("=====================================================");
        System.out.println(html);
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
