package com.hxx.sbConsole.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hxx.sbcommon.common.io.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成的token中不带有过期时间，token的过期时间由redis进行管理
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-16 10:17:44
 **/
@Slf4j
public class JwtUtil2 {
    //私钥
    private static final String TOKEN_SECRET = "123456";

    /**
     * 生成token，自定义过期时间 毫秒
     *
     * @param userTokenDTO
     * @return
     */
    public static <T> String generateToken(T userTokenDTO) {
        // 私钥和加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        // 设置头部信息
        Map<String, Object> header = new HashMap<>(2);
        header.put("Type", "Jwt");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header)
                .withClaim("token", JsonUtil.toJSON(userTokenDTO))
                //.withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 检验token是否正确
     *
     * @param token
     * @param tCls
     * @return
     */
    public static <T> T parseToken(String token, Class<T> tCls) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        String tokenInfo = jwt.getClaim("token").asString();
        return JsonUtil.parse(tokenInfo, tCls);
    }
}
