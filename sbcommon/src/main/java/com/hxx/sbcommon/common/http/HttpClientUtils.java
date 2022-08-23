package com.hxx.sbcommon.common.http;

import com.hxx.sbcommon.common.other.HexUtil;
import com.hxx.sbcommon.common.encrypt.AESUtil;
import com.hxx.sbcommon.common.encrypt.RSAUtil;
import com.hxx.sbcommon.common.json.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-25 17:03:26
 **/
@Slf4j
public class HttpClientUtils {

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            resultString = e.getMessage();
            log.info("http访问失败：" + e);

        } finally {
            try {
                response.close();
            } catch (IOException e) {
                log.info("response关闭失败：" + e);
            }
        }

        return resultString;
    }

    /**
     * post请求,签名和报文加密
     *
     * @param url        请求地址
     * @param json       请求json参数
     * @param appId      商户id
     * @param publicKey  rsa公钥
     * @param privateKey rsa私钥
     * @return
     */
    public static String doPostJsonForSign(String url, String json, String appId, String publicKey, String privateKey) throws Exception {
        String aseKey = appId.substring(0, 16);
        JsonRequest jsonRequest = new JsonRequest();
        jsonRequest.setRequestId(getUUID32());
        jsonRequest.setAppId(appId);
        jsonRequest.setTimestamp(System.currentTimeMillis());
        //aseKey 加密
        log.info("开始aseKey加密....");
        byte[] enStr = RSAUtil.encryptByPublicKey(aseKey, publicKey);
        String aseKeyStr = HexUtil.bytesToHexString(enStr);
        jsonRequest.setAseKey(aseKeyStr);
        //请求参数进行加密
        String body = "";
        try {
            log.info("开始请求参数加密....");
            body = AESUtil.encrypt(json, aseKey, appId.substring(16));
        } catch (Exception e) {
            log.info("报文加密异常：" + e);
            throw new Exception("报文加密异常", e);
        }
        jsonRequest.setBody(body);

        Map<String, Object> paramMap = RSAUtil.bean2Map(jsonRequest);
        paramMap.remove("sign");
        // 参数排序
        Map<String, Object> sortedMap = RSAUtil.sort(paramMap);
        // 拼接参数：key1Value1key2Value2
        String urlParams = RSAUtil.groupStringParam(sortedMap);
        //私钥签名
        log.info("开始参数签名....");
        String sign = RSAUtil.sign(HexUtil.hexStringToBytes(urlParams), privateKey);
        jsonRequest.setSign(sign);
        String requestParams = JacksonUtil.beanToJson(jsonRequest);
        log.info("发起请求....");
        String result = doPostJson(url, requestParams);
        return result;
    }

    public static String getUUID32() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        return uuid;
    }
}
