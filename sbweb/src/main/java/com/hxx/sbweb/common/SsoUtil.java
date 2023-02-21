package com.hxx.sbweb.common;

import java.security.PublicKey;

/**
 * SSO加密工具
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
public class SsoUtil {

    /** 公钥 */
    static PublicKey publicKey = null;

    static {
        try {
            publicKey = RsaUtil.getPublicKeyFromPem("RsaPublicKeySSO.pem");
        } catch (Exception e) {
        }
    }

    /**
     * 帐号数据加密
     *
     * @param content 内容
     * @return {@link String}
     */
    public static String encrypt(String content) {
        try {
            byte[] encryptedData = RsaUtil.encryptByPublicKey(content.getBytes(), publicKey);
            return DigestUtil.toBASE64NoFormat(encryptedData).replace("\n", "");
        } catch (Exception ex) {
            //log.error("sso信息Rsa加密失败", ex);
            return null;
        }
    }
}
