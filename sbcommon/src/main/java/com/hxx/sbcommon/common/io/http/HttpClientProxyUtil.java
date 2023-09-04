package com.hxx.sbcommon.common.io.http;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-01 17:18:46
 **/
public class HttpClientProxyUtil {

    public String proxyStr(String url, String proxyHost, int proxyPort, String username, String password) throws IOException {
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(proxyHost, proxyPort),
                new UsernamePasswordCredentials(username, password));
        CloseableHttpClient client = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);

        HttpPost httpPost = new HttpPost(url);
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .build();
        httpPost.setConfig(config);

        CloseableHttpResponse closeableHttpResponse = client.execute(httpPost);
        return EntityUtils.toString(closeableHttpResponse.getEntity());
    }

    void proxyCase1(String url, String proxyHost, int proxyPort) throws Exception {
        HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

        HttpPost httpost = new HttpPost(url);

//        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//        nvps.add(new BasicNameValuePair("param name", param));
//        httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.ISO_8859_1));

        HttpResponse response = httpclient.execute(httpost);

        HttpEntity entity = response.getEntity();
        System.out.println("Request Handled?: " + response.getStatusLine());
        InputStream in = entity.getContent();
        httpclient.getConnectionManager().shutdown();
    }

    void proxyCase2(){
        HttpHost proxy = new HttpHost("someproxy", 8080);
        HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy) {
            @Override
            public HttpRoute determineRoute(
                    final HttpHost host,
                    final HttpRequest request,
                    final HttpContext context) throws HttpException {
                String hostname = host.getHostName();
                if (hostname.equals("127.0.0.1") || hostname.equalsIgnoreCase("localhost")) {
                    // Return direct route
                    return new HttpRoute(host);
                }
                return super.determineRoute(host, request, context);
            }
        };
    }

}
