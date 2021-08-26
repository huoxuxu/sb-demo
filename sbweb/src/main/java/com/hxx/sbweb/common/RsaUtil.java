package com.hxx.sbweb.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * rsa跑龙套
 *
 * @author: 龚哲
 * @date 2020-04-26 9:59
 */
public class RsaUtil {

    /** 加密类型 */
    private final static String algorithm = "RSA";

    /** 签名算法 */
    private final static String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    /** 获取资源文件夹中的文件工具 */
    private final static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /**
     * 生成密钥对：密钥对中包含公钥和私钥
     *
     * @return 包含 RSA 公钥与私钥的 keyPair
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);    // 获得RSA密钥对的生成器实例
        SecureRandom secureRandom = new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8)); // 说的一个安全的随机数
        keyPairGenerator.initialize(1024, secureRandom);    // 这里可以是1024、2048 初始化一个密钥对
        return keyPairGenerator.generateKeyPair();   // 获得密钥对
    }

    /**
     * 获取公钥 (并进行Base64编码，返回一个 Base64 编码后的字符串)
     *
     * @param keyPair
     * @return 返回一个 Base64 编码后的公钥字符串
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 获取私钥(并进行Base64编码，返回一个 Base64 编码后的字符串)
     *
     * @param keyPair
     * @return 返回一个 Base64 编码后的私钥字符串
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 读取私钥Pem格式
     *
     * @param filePath 密钥文件名称（默认 RsaPrivateKey.pem）
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKeyFromPem(String filePath) throws Exception {
        if (StringUtils.isEmpty(filePath)) {
            filePath = "RsaPrivateKey.pem";
        }
        filePath = StringUtils2.ensureStartWith(filePath, "classpath:keys/");

        try {
            Resource pubKeyfile = resourceLoader.getResource(filePath);
            InputStream inputStream = pubKeyfile.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader br = new BufferedReader(inputStreamReader);
            br.readLine();// 过滤标识行
            String s = br.readLine();
            String str = "";
            while (s.charAt(0) != '-') {
                str += s + "\r";
                s = br.readLine();
            }
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] b = base64decoder.decodeBuffer(str);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b);
            PrivateKey privateKey = kf.generatePrivate(keySpec);

            return privateKey;
        } catch (Exception ex) {
            throw new Exception("私钥读取加载失败:" + filePath);
        }
    }

    /**
     * 读取公钥Pem格式
     *
     * @param filePath 密钥路径（默认用户信息加密密钥 RsaPublicKey，SSO加密需要单独指定）
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKeyFromPem(String filePath) throws Exception {
        if (StringUtils.isEmpty(filePath)) {
            filePath = "RsaPublicKey.pem";
        }
        filePath = StringUtils2.ensureStartWith(filePath, "classpath:keys/");

        try {
            Resource pubKeyfile = resourceLoader.getResource(filePath);
            InputStream inputStream = pubKeyfile.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            BufferedReader br = new BufferedReader(inputStreamReader);
            br.readLine();// 过滤标识行
            String s = br.readLine();
            String str = "";
            while (s.charAt(0) != '-') {
                str += s + "\r";
                s = br.readLine();
            }
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] b = base64decoder.decodeBuffer(str);
            KeyFactory kf = KeyFactory.getInstance(algorithm);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(b);
            PublicKey pubKey = kf.generatePublic(keySpec);

            return pubKey;
        } catch (Exception ex) {
            throw new Exception("公钥读取加载失败:" + filePath + ex.getMessage());
        }
    }

    /**
     * 私钥解密
     *
     * @param data       密文
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 用公钥解密
     *
     * @param data      密文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 用公钥加密
     *
     * @param data      明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 用私钥加密
     *
     * @param data       明文
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(algorithm).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return outStr;
    }

    /**
     * RSA公钥加密
     *
     * @param str
     * @param pubKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String str, PublicKey pubKey) throws Exception {
        //RSA加密
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = org.apache.commons.codec.binary.Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        return outStr;
    }

    /**
     * RSA私钥加密
     *
     * @param str        待加密内容
     * @param privateKey 私钥
     * @return
     */
    public static String encrypt(String str, PrivateKey privateKey) throws Exception {
        byte[] data = encryptByPrivateKey(str.getBytes(StandardCharsets.UTF_8), privateKey);
        return DigestUtil.toBASE64(data);
    }

    /**
     * RSA签名
     *
     * @param content 待签名数据
     * @param priKey  商户私钥
     * @return 签名值
     */
    public static String sign(String content, PrivateKey priKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));

            byte[] signed = signature.sign();
            return DigestUtil.toBASE64(signed);
        } catch (Exception e) {
            throw new RuntimeException("签名发生异常", e);
        }
    }

    /**
     * RSA 验签
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(StandardCharsets.UTF_8));

            return signature.verify(DigestUtil.fromBASE64toByte(sign));
        } catch (Exception e) {
            throw new RuntimeException("RSA 验签失败", e);
        }
    }
}
