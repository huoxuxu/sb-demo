package com.hxx.sbweb.common;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * 编码工具
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
public class DigestUtil {

    /**
     * base64进行加密
     *
     * @param data 待编码数据
     * @return 编码数据
     */
    public static String encryptBASE64(byte[] data) {
        return (new BASE64Encoder()).encodeBuffer(data);
    }

    /**
     * md5加密
     *
     * @param data 代编码数据
     * @return 编码数据
     * @throws Exception 异常
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    /**
     * 转为MD
     *
     * @param text    待签名内容
     * @param charset 字符集
     * @return {@link String}
     */
    public static String toMd5(String text, Charset charset) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            if (charset == null) {
                charset = StandardCharsets.UTF_8;
            }
            byte[] textBytes = text.getBytes(charset);
            byte[] md5bytes = encryptMD5(textBytes);

            //将加密后的数据转换为16进制数字
            String md5code = new BigInteger(1, md5bytes).toString(16);
            // 如果生成数字未满32位，需要前面补0
            for (int i = 0; i < 32 - md5code.length(); i++) {
                md5code = "0" + md5code;
            }
            return md5code;
        } catch (Exception e) {
            throw new RuntimeException("没有md5这个算法！");
        }
    }

    /**
     * 摘要生成
     *
     * @param data    请求数据
     * @param sign    签名秘钥(key或者parternID)
     * @param charset 编码格式
     * @return 经过md5=>base64编码=>字符串
     * @throws Exception ""
     */
    public static String digest(String data, String sign, String charset) throws Exception {
        return encryptBASE64(encryptMD5((data + sign).getBytes(charset))).trim();
    }

    /**
     * 字节数据 => BASE64编码
     *
     * @param byteArray 待编码数据
     * @return Base64字符串
     */
    public static String toBASE64(byte[] byteArray) {
        return new BASE64Encoder().encode(byteArray);
    }

    /**
     * 字节数据=> Base64编码 （去除换行符号）
     *
     * @param byteArray 带编码数据
     * @return Base64字符串
     */
    public static String toBASE64NoFormat(byte[] byteArray) {
        return new BASE64Encoder().encode(byteArray).replace("\r\n", "");
    }

    /**
     * 字符串 => BASE64编码
     *
     * @param str     待编码字符
     * @param charset 编码
     * @return 转码base64后字符
     */
    public static String toBASE64(String str, Charset charset) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        return new BASE64Encoder().encode(str.getBytes(charset));
    }

    /**
     * 解码Base64字符串，转为byte[]
     *
     * @param base64EncodedString Base64字符串
     * @return 字节数组
     */
    public static byte[] fromBASE64toByte(String base64EncodedString) throws IOException {
        if (StringUtils.isEmpty(base64EncodedString)) {
            return null;
        }
        return new BASE64Decoder().decodeBuffer(base64EncodedString);
    }

    /**
     * 解码base64字符串，转为string
     *
     * @param base64EncodedString Base64字符串
     * @return 字符串
     */
    public static String fromBASE64toStr(String base64EncodedString) throws IOException {
        if (StringUtils.isEmpty(base64EncodedString)) {
            return null;
        }
        return new String(new BASE64Decoder().decodeBuffer(base64EncodedString));
    }

    /**
     * 字节数组转十六进制
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 十六进制转字节数组
     *
     * @param inHex hex字符串
     * @return 字节数组
     */
    public static byte[] hexToByte(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = (byte) Integer.parseInt(inHex.substring(i, i + 2), 16);
            j++;
        }
        return result;
    }

    /**
     * 加签校验
     *
     * @param param
     * @param oldDigest
     * @return
     * @throws Exception
     */
    public static boolean checkDigest(String param, String oldDigest, String key) throws Exception {
        String newKey = digest(param, key, StandardCharsets.UTF_8.name());
        // log.info("前台签名:{},后台签名:{}", oldDigest, newKey);
        return oldDigest.equals(newKey);
    }
}