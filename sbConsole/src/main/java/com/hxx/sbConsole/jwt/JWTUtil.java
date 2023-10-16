package com.hxx.sbConsole.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-04 13:09:57
 **/
public class JWTUtil {
    private static final String TOKEN_SECRET = "eyJhbhbbO";
    private static final String CLAIM_NAME = "sign";
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 1000;

    /**
     * 加签
     *
     * @param sign
     * @param expireTimeMs 距离当前时间的过期时间，毫秒
     * @return
     */
    public static String sign(String sign, long expireTimeMs) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        Map<String, Object> header = new HashMap<>();
        {
            header.put("typ", "JWT");
            header.put("alg", "HS256");
        }

        expireTimeMs = expireTimeMs == 0 ? EXPIRE_TIME : expireTimeMs;
        Date date = new Date(System.currentTimeMillis() + expireTimeMs);
        String token = JWT.create()
                .withHeader(header)
                .withClaim(CLAIM_NAME, sign)
                .withExpiresAt(date)
                .sign(algorithm);
        return token;
    }

    /**
     * 验签
     *
     * @param token
     * @return
     */
    public static String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(CLAIM_NAME)
                    .asString();
        } catch (Exception e) {
            return null;
        }
    }
}
