package com.hxx.sbConsole.service.impl.http;

import com.hxx.sbConsole.external.okHttpClient.OkHttpClientHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-18 14:49:24
 **/
@Service
public class OkHttpClientServiceImpl {

    public static void demo() {

    }

    static void demo1() throws Exception {
        OkHttpClientHelper okHttp = new OkHttpClientHelper();
        {
            Map<String, String> headerMap = new HashMap<>();
            {
                headerMap.put("h1", "1");
                headerMap.put("h2", "2");
            }
            String html = okHttp.get("http://www.bing.com", headerMap);
            System.out.println(html);
        }
        {
            String html = okHttp.get("http://www.bing.com");
            System.out.println(html);
        }

    }

    public static void main(String[] args) {
        try {
            demo1();
        } catch (Exception e) {
            System.out.println(e + "");
        }
        System.out.println("ok");
    }
}
