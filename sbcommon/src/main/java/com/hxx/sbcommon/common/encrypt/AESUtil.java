package com.hxx.sbcommon.common.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-25 16:52:34
 **/
public class AESUtil {

    public static void main(String[] args) throws Exception {
        String encrypt = AESUtil.encrypt("你好啊！！！", "1234567890123456", "1234567890123456");
        String decrypt = AESUtil.decrypt(encrypt, "1234567890123456", "1234567890123456");
        System.out.println(decrypt);
    }

    /**
     * 加密
     * @param content 加密文本
     * @param key 加密密钥，appSecret的前16位
     * @param iv 初始化向量，appSecret的后16位
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key, String iv) throws Exception {
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //"算法/模式/补码方式"
        IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes()); //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParam);
        byte[] encrypted = cipher.doFinal(content.getBytes());

        return new BASE64Encoder().encode(encrypted);
    }

    public static String decrypt(String content, String key, String iv) throws Exception {
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding "); //"算法/模式/补码方式"
        IvParameterSpec ivParam = new IvParameterSpec(iv.getBytes()); //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParam);
        byte[] encrypted = new BASE64Decoder().decodeBuffer(content); //先用base64解密
        byte[] original = cipher.doFinal(encrypted);
        return new String(original);
    }

}
