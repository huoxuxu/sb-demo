package com.hxx.sbweb.common;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * jwt工具
 *
 * @author xiyunfei
 * @date 2021/02/03
 */
public class JwtUtil {
    /**
     * 生成token
     *
     * @param payload    内容有效载荷
     * @param secretKey  token私密
     * @param expireTime 失效时间
     * @return
     */
    public static String generateJwt(String payload, String secretKey, long expireTime) {
        //签名算法，选择SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);
        JwtBuilder builder = Jwts.builder()
                .setSubject(payload)
                .signWith(signatureAlgorithm, secretKey)
                .setIssuedAt(now);
        if (expireTime > 0) {
            long expireMills = nowMills + expireTime;
            Date expireDate = new Date(expireMills);
            //过期时间
            builder.setExpiration(expireDate);
        }
        return builder.compact();
    }

    /**
     * 校验token
     *
     * @param jwt
     * @param secretKey
     * @return
     */
    public static boolean validateJwt(String jwt, String secretKey) {
        try {
            parseJwt(jwt, secretKey);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String parseJwt(String jwt, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
