package com.hxx.sbweb.common;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @Description: 加密解密工具类
 * @Author: fangyanlong
 * @Date: 2019-06-10 14:57
 */
public class EncoderUtil {
    public static String aESEncode(String encodeRules, String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteEncode = content.getBytes("utf-8");
            byte[] byteAES = cipher.doFinal(byteEncode);
            String aESEncode = Base64.getEncoder().encodeToString(byteAES);
            return aESEncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String aESDecode(String encodeRules, String content) {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128, new SecureRandom(encodeRules.getBytes()));
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey key = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteContent = Base64.getDecoder().decode(content);
            byte[] byteDecode = cipher.doFinal(byteContent);
            String aESDecode = new String(byteDecode, "utf-8");
            return aESDecode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}