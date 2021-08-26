package com.hxx.sbweb.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 令牌代理工具
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
@Data
public class TokenProviderUtil {

    /** 获取资源文件夹中的文件工具 */
    final static ResourceLoader resourceLoader = new DefaultResourceLoader();

    /** 密钥 签发方用私钥， */
    @Getter
    @Setter
    private PrivateKey prvKey;

    /** 公钥 验证方用公钥 */
    @Getter
    @Setter
    private PublicKey pubKey;

    /**
     * 读取密钥
     *
     * @return {@link Boolean}
     */
    public boolean ReadKey() {
        Resource pubKey = resourceLoader.getResource("classpath:keys/RsaPublicKey.pem");
        Resource priKey = resourceLoader.getResource("classpath:keys/RsaPrivateKey.pem");

        try {
            ObjectInputStream in_pri = new ObjectInputStream(pubKey.getInputStream());
            setPubKey((PublicKey) in_pri.readObject());
            in_pri.close();

            ObjectInputStream in_pri2 = new ObjectInputStream(priKey.getInputStream());
            setPrvKey((PrivateKey) in_pri2.readObject());
            in_pri2.close();

            return !(getPubKey() == null || getPrvKey() == null);
        } catch (Exception ex) {
            //log.error("加载DSA密钥失败:", ex);
            return false;
        }
    }

    /**
     * 读取RSA密钥
     *
     * @return boolean
     */
    public boolean ReadRSAKey() {
        try {
            setPrvKey(RsaUtil.getPrivateKeyFromPem(null));
            setPubKey(RsaUtil.getPublicKeyFromPem(null));
            return (getPubKey() != null && getPrvKey() != null);
        } catch (Exception e) {
            //log.error("令牌加载RSA私钥失败:", e);
            return false;
        }
    }

    /**
     * 编码用户和有效期得到令牌
     *
     * @param str    设备#用户
     * @param expire 过期时间
     * @return {@link String}
     */
    public String Encode(String str, Date expire) {
        // 设备#用户,过期使用=数据
        String data = str + "," + expire.getTime();
        // rsa签名
        String signData = RsaUtil.sign(data, getPrvKey());
        // 过滤换行符，避免在客户端使用令牌时出现请求头解析错误
        signData = signData.replaceAll("(\r\n|\n)", "");
        return DigestUtil.toBASE64(data.getBytes(StandardCharsets.UTF_8)) + "." + signData;
    }

    /**
     * 尝试解码令牌，即使失败，也会返回用户信息和有效时间
     *
     * @param token 令牌
     * @return 解码结果，成功或失败
     * @throws Exception 异常
     */
    public Map<String, Object> TryDecode(String token) throws Exception {
        if (StringUtils.isEmpty(token) || token.indexOf(".") == -1) {
            throw new Exception("令牌异常");
        }
        int p = token.indexOf(".");
        String data = token.substring(0, p);
        String signDate = token.substring(p + 1);

        data = DigestUtil.fromBASE64toStr(data);
        p = data.indexOf(",");
        if (p == -1) {
            throw new Exception("加密数据异常");
        }
        String userData = data.substring(0, p);
        String timeData = data.substring(p + 1);

        Date expDate = DateUtil.getDateFromStamp(timeData);
        p = userData.indexOf("#");
        if (p == -1) {
            throw new Exception("用户数据异常");
        }
        String deviceCode = userData.substring(0, p);
        String userCode = userData.substring(p + 1);

        // 验证签名
        boolean status = RsaUtil.verify(data, signDate, getPubKey());
        // 验证失败
        if (!status) {
            throw new Exception("解码失败");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("UserCode", userCode);
        map.put("DeviceCode", deviceCode);
        map.put("ExpireTime", expDate);

        return map;
    }
}
