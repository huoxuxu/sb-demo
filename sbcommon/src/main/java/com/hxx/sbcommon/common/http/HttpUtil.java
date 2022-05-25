package com.hxx.sbcommon.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 16:55:12
 **/
public class HttpUtil {
    public HttpUtil() {
    }

    public static String sendPostJson(String httpUrl, String json) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection)(new URL(httpUrl)).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        httpURLConnection.getOutputStream().write(json.getBytes("utf-8"));
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getOutputStream().close();
        StringBuffer sb = new StringBuffer();
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));

        String readLine;
        while((readLine = responseReader.readLine()) != null) {
            sb.append(readLine);
        }

        String result = sb.toString();
        responseReader.close();
        return result;
    }
}
