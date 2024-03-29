package com.hxx.sbConsole.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hxx.sbConsole.commons.jwt.JWTUtil;
import com.hxx.sbConsole.model.Dog;
import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.model.enums.LinePatternEnum;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandlerFactory;
import com.hxx.sbcommon.common.basic.langType.impl.ListTypeHandler;
import com.hxx.sbcommon.common.hardware.NetUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.CopyObjUtil;
import com.hxx.sbcommon.common.reflect.MethodSignature;
import com.hxx.sbcommon.common.reflect.ReflectorObj;
import com.hxx.sbcommon.model.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("==================================================");
        {
            Method[] methods = User.class.getMethods();
            for (Method method : methods) {
                String signStr = MethodSignature.getMethodSignature(method);
                System.out.println(signStr);
            }
        }
        System.out.println("==================================================");
        {
            User user = new User();
            {
                user.setId(123);
                user.setCode("ls");
                user.setName("里斯");
            }

            ReflectorObj rd = new ReflectorObj(User.class);
            Map<String, Object> objMap = rd.getObjMap(user);
            System.out.println("userMap：" + JsonUtil.toJSON(objMap));

            Map<String, Object> map = BeanInfoUtil.toMap(user);
            System.out.println("user对象：" + JsonUtil.toJSON(user));
            System.out.println("userMap：" + JsonUtil.toJSON(map));

            User dest = new User();
            CopyObjUtil.copyTo(user, dest);
            System.out.println("dest：" + JsonUtil.toJSON(dest));

            Dog dog = new Dog();
            CopyObjUtil.copyToObj(user, dog);
            System.out.println("dog：" + JsonUtil.toJSON(dog));

            // =================泛型字段===================================
            Result<String> result = new Result<>(1, "xx", "m12");
            ReflectorObj rdf = new ReflectorObj(result.getClass());
            Map<String, Object> objMap1 = rdf.getObjMap(result);
            System.out.println("objMap：" + JsonUtil.toJSON(objMap1));
        }
        System.out.println("==================================================");
        {
            ReflectorObj rd = new ReflectorObj(User.class);

            Map<String, Object> map = new HashMap<>();
            map.put("id", 111);
            map.put("code", "zs");
            map.put("name", "张三");

            User user = rd.setInstance(map);
            System.out.println(JsonUtil.toJSON(user));
        }
        System.out.println("==================================================");
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
        System.out.println("==================================================");
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
        System.out.println("==================================================");
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
        System.out.println("==================================================");
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
        System.out.println("==================================================");
        {
            List<InetAddress> ls = NetUtil.getIPV4InetAddress();
            System.out.println(ls);
            List<InetAddress> ls1 = NetUtil.getIPV4InetAddress("Intel");
            System.out.println(ls1);
            String localIP = NetUtil.getLocalIP();
            System.out.println(localIP);
        }
        System.out.println("==================================================");
        // LangTypeHandlerFactory
        {
            // HashMap
            {
                {
                    Map<String, Object> map = new HashMap<>();
                    map.put("1", "11");
                    map.put("2", 22);
                    Object result = LangTypeHandlerFactory.convert(map, Map.class);
                    System.out.println(result);
                }
                {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("1", "11");
                    map.put("2", 22);
                    Object result = LangTypeHandlerFactory.convert(map, Map.class);
                    System.out.println(result);
                }
                {
                    String json = "{\"code\":\"11\",\"name\":22}";
                    Object result = LangTypeHandlerFactory.convert(json, Map.class);
                    System.out.println(result);
                }
            }
            // List
            {
                ListTypeHandler lt = new ListTypeHandler();
                {
                    // [1,2,3]
                    int[] arr = {1, 2, 3};
                    List<?> ls = lt.convert(arr);
                    System.out.println(ls);
                }
                {
                    // [1,2,3]
                    Integer[] arr = {1, 2, 3};
                    List<?> ls = lt.convert(arr);
                    System.out.println(ls);
                }
                {
                    // [1,2,3]
                    List<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3));
                    List<?> ls = lt.convert(arr);
                    System.out.println(ls);
                }
                {
                    // 暂不支持
                    // ["code","code1"]
//                    String json = "[\"code\",\"code1\"]";
//                    List<?> ls = lt.convert(json);
//                    System.out.println(ls);
                }
            }
            // 枚举
            {
                Class<LinePatternEnum> enumCls = LinePatternEnum.class;
                LinePatternEnum linePatternEnum = LinePatternEnum.BIG;
                {
                    // 测试同类型
                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(linePatternEnum, enumCls);
                    System.out.println(eval);
                }
                {
                    // 测试字符串
                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert("BIG", enumCls);
                    System.out.println(eval);
                }
                {
                    // 测试字符串  不通过
//                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert("big", enumCls);
//                    System.out.println(eval);
                }
                {
                    // 测试 指定枚举索引位
                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(1, enumCls);
                    System.out.println(eval);
                }
                {
                    // 测试  指定枚举索引位
                    LinePatternEnum eval = (LinePatternEnum) LangTypeHandlerFactory.convert(0L, enumCls);
                    System.out.println(eval);
                }
            }
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
