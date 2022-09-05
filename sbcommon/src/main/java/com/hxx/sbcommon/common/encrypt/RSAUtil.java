package com.hxx.sbcommon.common.encrypt;

import com.alibaba.fastjson.JSON;
import com.hxx.sbcommon.common.encoder.Base64Util;
import com.hxx.sbcommon.common.other.HexUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-25 16:53:38
 **/
public class RSAUtil {
    /**
     * 定义加密方式
     */
    private final static String KEY_RSA = "RSA";
    /**
     * 定义签名算法
     */
    private final static String KEY_RSA_SIGNATURE = "MD5withRSA";
    /**
     * 定义公钥算法
     */
    private final static String KEY_RSA_PUBLICKEY = "RSAPublicKey";
    /**
     * 定义私钥算法
     */
    private final static String KEY_RSA_PRIVATEKEY = "RSAPrivateKey";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 初始化密钥
     */
    public static Map<String, Object> init() {
        Map<String, Object> map = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_RSA);
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();
            // 公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 将密钥封装为map
            map = new HashMap<>();
            map.put(KEY_RSA_PUBLICKEY, publicKey);
            map.put(KEY_RSA_PRIVATEKEY, privateKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  公钥
     */
    public static byte[] encryptByPublicKey(String data, String key) {
        byte[] result = null;
        try {
            byte[] bytes = decryptBase64(key);
            // 取得公钥
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PublicKey publicKey = factory.generatePublic(keySpec);
            // 对数据加密
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encode = cipher.doFinal(data.getBytes());
            // 再进行Base64加密
            result = Base64Util.encode(encode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 私钥解密
     *
     * @param data 加密数据
     * @param key  私钥
     */
    public static String decryptByPrivateKey(byte[] data, String key) {
        String result = null;
        try {
            // 对私钥解密
            byte[] bytes = decryptBase64(key);
            // 取得私钥
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            PrivateKey privateKey = factory.generatePrivate(keySpec);
            // 对数据解密
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            // 先Base64解密
            byte[] decoded = Base64Util.decode(data);
            result = new String(cipher.doFinal(decoded));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取公钥
     */
    public static String getPublicKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PUBLICKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取私钥
     */
    public static String getPrivateKey(Map<String, Object> map) {
        String str = "";
        try {
            Key key = (Key) map.get(KEY_RSA_PRIVATEKEY);
            str = encryptBase64(key.getEncoded());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     */
    public static String sign(byte[] data, String privateKey) {
        String str = "";
        try {
            // 解密由base64编码的私钥
            byte[] bytes = decryptBase64(privateKey);
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs = new PKCS8EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取私钥对象
            PrivateKey key = factory.generatePrivate(pkcs);
            // 用私钥对信息生成数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initSign(key);
            signature.update(data);
            str = encryptBase64(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true，失败返回false
     */
    public static boolean verify(byte[] data, String publicKey, String sign) {
        boolean flag = false;
        try {
            // 解密由base64编码的公钥
            byte[] bytes = decryptBase64(publicKey);
            // 构造X509EncodedKeySpec对象
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
            // 指定的加密算法
            KeyFactory factory = KeyFactory.getInstance(KEY_RSA);
            // 取公钥对象
            PublicKey key = factory.generatePublic(keySpec);
            // 用公钥验证数字签名
            Signature signature = Signature.getInstance(KEY_RSA_SIGNATURE);
            signature.initVerify(key);
            signature.update(data);
            flag = signature.verify(decryptBase64(sign));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * BASE64 解密
     *
     * @param key 需要解密的字符串
     * @return 字节数组
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return new BASE64Decoder().decodeBuffer(key);
    }

    /**
     * BASE64 加密
     *
     * @param key 需要加密的字节数组
     * @return 字符串
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return new BASE64Encoder().encode(key);
    }

    /**
     * 按照红黑树（Red-Black tree）的 NavigableMap 实现
     * 按照字母大小排序
     */
    public static Map<String, Object> sort(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        Map<String, Object> result = new TreeMap<>((Comparator<String>) (o1, o2) -> {
            return o1.compareTo(o2);
        });
        result.putAll(map);
        return result;
    }

    /**
     * 组合参数
     *
     * @param map
     * @return 如：key1Value1Key2Value2....
     */
    public static String groupStringParam(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> item : map.entrySet()) {
            if (item.getValue() != null) {
                sb.append(item.getKey());
                if (item.getValue() instanceof List) {
                    sb.append(JSON.toJSONString(item.getValue()));
                } else {
                    sb.append(item.getValue());
                }
            }
        }
        return sb.toString();
    }

    /**
     * bean转map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> bean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (ObjectUtils.isEmpty(value)) {
                        continue;
                    }
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

//    /**
//     * 按照红黑树（Red-Black tree）的 NavigableMap 实现
//     * 按照字母大小排序
//     */
//    public static Map<String, Object> sort(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        }
//        Map<String, Object> result = new TreeMap<>((Comparator<String>) (o1, o2) -> {
//            return o1.compareTo(o2);
//        });
//        result.putAll(map);
//        return result;
//    }
//
//    /**
//     * 组合参数
//     *
//     * @param map
//     * @return 如：key1Value1Key2Value2....
//     */
//    public static String groupStringParam(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        }
//        StringBuffer sb = new StringBuffer();
//        for (Map.Entry<String, Object> item : map.entrySet()) {
//            if (item.getValue() != null) {
//                sb.append(item.getKey());
//                if (item.getValue() instanceof List) {
//                    sb.append(JSON.toJSONString(item.getValue()));
//                } else {
//                    sb.append(item.getValue());
//                }
//            }
//        }
//        return sb.toString();
//    }
//
//    /**
//     * bean转map
//     * @param obj
//     * @return
//     */
//    public static Map<String, Object> bean2Map(Object obj) {
//        if (obj == null) {
//            return null;
//        }
//        Map<String, Object> map = new HashMap<>();
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//            for (PropertyDescriptor property : propertyDescriptors) {
//                String key = property.getName();
//                // 过滤class属性
//                if (!key.equals("class")) {
//                    // 得到property对应的getter方法
//                    Method getter = property.getReadMethod();
//                    Object value = getter.invoke(obj);
//                    if (ObjectUtils.isEmpty(value)) {
//                        continue;
//                    }
//                    map.put(key, value);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return map;
//    }


    public static void main(String[] args) throws Exception {
        String publicKey = "";
        String privateKey = "";

        System.out.println("公钥加密======私钥解密");
        String str = "Longer程序员";
        byte[] enStr = RSAUtil.encryptByPublicKey(str, publicKey);
        String miyaoStr = HexUtil.bytesToHexString(enStr);
        byte[] bytes = HexUtil.hexStringToBytes(miyaoStr);
        String decStr = RSAUtil.decryptByPrivateKey(bytes, privateKey);
        System.out.println("加密前：" + str + "\n\r解密后：" + decStr);

        System.out.println("\n\r");
        System.out.println("私钥签名======公钥验证");
        String sign = RSAUtil.sign(str.getBytes(), privateKey);
        System.out.println("签名：\n\r" + sign);
        boolean flag = RSAUtil.verify(str.getBytes(), publicKey, sign);
        System.out.println("验签结果：\n\r" + flag);
    }
}
