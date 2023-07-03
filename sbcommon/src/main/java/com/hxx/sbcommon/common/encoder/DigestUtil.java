package com.hxx.sbcommon.common.encoder;

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
}
