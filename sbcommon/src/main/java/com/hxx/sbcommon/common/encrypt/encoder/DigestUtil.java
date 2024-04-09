package com.hxx.sbcommon.common.encrypt.encoder;

import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-29 9:15:54
 **/
public class DigestUtil {
    public static final String UTF8 = "UTF-8";

    /**
     * 不加密
     *
     * @param data
     * @param charset
     * @return
     * @throws Exception
     * @description
     */
    public static String digestDZBP(String data, String charset) throws Exception {
        String t = encryptBASE64(data.getBytes(charset));
        return t.trim();
    }

    /**
     * base64
     *
     * @param md5
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] md5) throws Exception {
        return new String(Base64.getEncoder().encode(md5), "UTF-8");
    }

    /**
     * MD5
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    /**
     * 摘要生成
     *
     * @param data    请求数据
     * @param sign    签名秘钥(key或者parternID)
     * @param charset 编码格式
     * @return 摘要
     * @throws Exception
     */
    public static String digest(String data, String sign, String charset) throws Exception {
        String t = encryptBASE64(encryptMD5((data + sign).getBytes(charset)));
        return t.trim();
    }

    // 用这个方法
    // data+key作为这里的str
    public static String md5WithBase64(String str, String charset) {
        return base64(md5ToBytes(str, charset));
    }

    public static String base64(byte[] bytes) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public static String decodeBase64(String content, Charset charset) {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(content), charset);
    }

    public static String decodeBase64(String content) {
        return new String(org.apache.commons.codec.binary.Base64.decodeBase64(content));
    }

    public static byte[] md5ToBytes(String str) {
        return md5ToBytes(str, "UTF-8");
    }

    public static byte[] md5ToBytes(String str, String charset) {
        return DigestUtils.md5(str.getBytes(Charset.forName(charset)));
    }

    public static String md5ToStr(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String md5ToStr(String str, Charset charset) {
        return DigestUtils.md5Hex(str.getBytes(charset));
    }

}
