package com.hxx.sbConsole.service.impl.http;

import com.alibaba.fastjson.JSONObject;
import com.hxx.sbConsole.model.HttpTestDemoModel;
import com.hxx.sbcommon.common.http.HttpClientUtil;
import com.hxx.sbcommon.common.json.FastJsonReaderQuick;
import com.hxx.sbcommon.common.json.JsonUtil;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-05 12:59:30
 **/
public class HttpClientServiceDemo {
    public static void main(String[] args) throws Exception {
        String url = "http://127.0.0.1:666/HttpTest/Index";
        {
            Map<String, String> headers = new HashMap<>();
            {
                headers.put("a", "a1");
            }
            final String html;
            {
                html = HttpClientUtil.get(url);
            }
            {
                HttpClientUtil.get(url, headers, null, HttpClientServiceDemo::parseReader);
            }
            System.out.println(html);
        }
        System.out.println("=====================================================");

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
