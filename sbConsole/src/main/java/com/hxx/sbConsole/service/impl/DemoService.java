package com.hxx.sbConsole.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hxx.sbConsole.commons.jwt.JWTUtil;
import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.thread.ThreadDemo;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.hardware.NetUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.reflect.BeanConvertMapUtils;
import com.hxx.sbcommon.common.reflect.ReflectorObjSetter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import sun.net.util.IPAddressUtil;

import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.l;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:17:53
 **/
@Service
public class DemoService {
    public static void demo() throws Exception {
        {
            String hxy = OftenUtil.StringUtil.lowerFirstChar("HXY");
            System.out.println(hxy);
        }
        {
            User user = new User();
            {
                user.setId(123);
                user.setCode("ls");
                user.setName("里斯");
            }

            Map<String, Object> map = BeanConvertMapUtils.toMap(user);
            System.out.println(JsonUtil.toJSON(map));
        }
        {
            ReflectorObjSetter rd = new ReflectorObjSetter(User.class);

            Map<String, Object> map = new HashMap<>();
            map.put("id", 111);
            map.put("code", "zs");
            map.put("name", "张三");

            User user = rd.setInstance(map);
            System.out.println(JsonUtil.toJSON(user));
        }
        {
            Map<String, Integer> map = new HashMap<>();
            {
                map.put("AAA", 1);
                map.put("BBB", 2);
                map.put("CCC", 3);
            }
            for (int i = 0; i < 10; i++) {
                String str = OftenUtil.weightRandom(map);
                System.out.println(i + ": " + str);
            }
        }
        {
            //加密的数据
            String userId = "51";
            //加密的签名
            Algorithm algorithm = Algorithm.HMAC256("eyJhbhbbO");
            //加密头
            Map<String, Object> header = new HashMap<String, Object>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");

            //超时时间
            int expireTime = 30 * 1000;
            Date date = new Date(System.currentTimeMillis() + expireTime);
            //令牌token
            String token = JWT.create()
                    .withHeader(header)
                    .withClaim("sign", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
            System.out.println(token);

            String sign = JWTUtil.sign(userId, expireTime);
            System.out.println(sign);

            String v1 = JWTUtil.verify(token);
            String v2 = JWTUtil.verify(sign);
            System.out.println(v1);
        }
        {
//            //前端传过来的令牌
//            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Nzg1NTEyOTgsInVzZXJJZCI6ImhlbGxvIn0.mVXwBkBMO-Sr3FJPZSVLezUuxdvMwNsGgRqTCVufH8o";
//            // 和上面一样的签名
//            Algorithm algorithm = Algorithm.HMAC256("eyJhbhbbO");
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .build();
//            //开始解密，失败的话会出现异常
//            DecodedJWT jwt = verifier.verify(token);
//            System.out.println(jwt.getClaim("userId")
//                    .asInt());"""
        }
        {
            String str = "001,002,003==004==002==002==001==001,002==005,006";
            String[] arr = str.split("==");
            //arr 按长度排序
            List<String> ls = Arrays.stream(arr)
                    .sorted(Comparator.comparing(String::length, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            List<List<String>> maxls = new ArrayList<>();
            Set<String> set1 = new HashSet<>();
            for (String item : ls) {
                if (item.contains(",")) {
                    List<String> arr1 = Arrays.stream(item.split(","))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(maxls)) {
                        maxls.add(arr1);
                    } else {
                        // 判断 maxls 集合是否包含此项
                        boolean flag = maxls.stream()
                                .allMatch(d -> CollectionUtils.isEmpty(CollectionUtils.intersection(d, arr1)));
                        if (flag) {
                            maxls.add(arr1);
                        }
                    }
                } else {
                    set1.add(item);
                }
            }

            if (CollectionUtils.isEmpty(maxls)) {
                // 输出
                System.out.println(set1);
            } else {
                System.out.println(maxls);
                for (String item : set1) {
                    if (maxls.stream()
                            .noneMatch(d -> d.contains(item))) {
                        System.out.println(item);
                    }
                }
            }
        }
        {
            List<InetAddress> ls = NetUtil.getIPV4InetAddress();
            List<InetAddress> ls1 = NetUtil.getIPV4InetAddress("Intel");
            String localIP = NetUtil.getLocalIP();
            System.out.println(localIP);
        }
    }


    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
