package com.hxx.sbcommon.common.encrypt;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

public class RsaUtils {

    public static final Logger logger = LoggerFactory.getLogger(RsaUtils.class);

    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str) {
        //公钥
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVMs0M9TBKtcZaI5VurEdvNGRkODFVjhUc+t29VZzDvQ6MBrRivTQtboCtNCNuzFH48zZ604K6g6IjL1S+KPCABVlOx+AYa/lGiRiVtd6Gn+MMXNEJkik2rVb6Gr8bx8I7XfVmNEaJuM/BlyCAglhfaw7E6UQa0ry7Vt3IMbMVPwIDAQAB";
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        try {
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            logger.error("RSA加密错误 {}", ExceptionUtils.getStackTrace(e));
            return "";
        }
    }
}
