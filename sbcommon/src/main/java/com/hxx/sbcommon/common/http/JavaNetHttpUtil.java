package com.hxx.sbcommon.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-02 10:30:56
 **/
public class JavaNetHttpUtil {

    /**
     * 发起Post请求
     * @param httpUrl
     * @param json
     * @return
     * @throws IOException
     */
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
        httpURLConnection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        httpURLConnection.getOutputStream().flush();
        httpURLConnection.getOutputStream().close();
        StringBuilder sb = new StringBuilder();
        try(BufferedReader responseReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8))) {
            String readLine;
            while ((readLine = responseReader.readLine()) != null) {
                sb.append(readLine);
            }

            String result = sb.toString();
            responseReader.close();
            return result;
        }
    }

}
