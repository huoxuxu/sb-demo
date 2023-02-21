package com.hxx.sbcommon.common.other.encoder;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-02 10:33:12
 **/
public class DigestUtil {
    public static final String GBK = "GBK";
    public static final String UTF8 = "UTF-8";

    public DigestUtil() {
    }

    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static String encryptBASE64(byte[] md5) throws Exception {
        Base64.Encoder encoder = Base64.getEncoder();
        String encode = encoder.encodeToString(md5);
        return encode;
    }

    public static String digest(String data, String sign, String charset) throws Exception {
        String t = encryptBASE64(encryptMD5((data + sign).getBytes(charset)));
        if ("\n".equals(System.getProperty("line.separator"))) {
            String t2 = t.replaceAll("\\n", "\r\n");
            return t2;
        } else {
            return t;
        }
    }

}
