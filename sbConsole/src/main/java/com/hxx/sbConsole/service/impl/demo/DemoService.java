package com.hxx.sbConsole.service.impl.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hxx.sbConsole.commons.jwt.JWTUtil;
import com.hxx.sbConsole.model.Dog;
import com.hxx.sbConsole.model.User;
import com.hxx.sbConsole.service.impl.HttpTxtParser;
import com.hxx.sbConsole.service.impl.demo.basic.TypeHandlerDemoService;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.hardware.NetUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.other.pageSeparate.GeneralPageable;
import com.hxx.sbcommon.common.other.pageSeparate.PageSeparate;
import com.hxx.sbcommon.common.other.pageSeparate.Pageable;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.CopyObjUtil;
import com.hxx.sbcommon.common.reflect.MethodSignature;
import com.hxx.sbcommon.common.reflect.ReflectorObj;
import com.hxx.sbcommon.model.Result;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.io.File;
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
    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void demo() throws Exception {
        System.out.println("BasicType==================================================");
        {
            {
                // 32位单精度浮点数的有效位数是7位。
                // 64位双精度浮点数的有效位数是16位。
                Float dVal = 123456789.12345678F;
                System.out.println(dVal);
                dVal = 0.123456789F;
                System.out.println(dVal);
            }
        }
        System.out.println("PageSeparate==================================================");
        {
            Pageable page = new GeneralPageable(1, 10);
            PageSeparate pageSeparate = new PageSeparate.MySQLPageSeparate(page);
            String pageSql = pageSeparate.getPageSql("select * from demo");
            System.out.println(pageSql);
        }
        System.out.println("System==================================================");
        {
            String item = null;
            String s = OftenUtil.BasicUtil.procFieldNull(item, "");
            System.out.println(s);

            {
                String clsFullName = "java.util.List";
                Class<?> cls = Class.forName(clsFullName);
                System.out.println(cls);
                if (Collection.class.isAssignableFrom(cls)) {
                    System.out.println("true");
                }
            }
            {
                String clsFullName = "java.util.Map";
                Class<?> cls = Class.forName(clsFullName);
                System.out.println(cls);
                if (!Collection.class.isAssignableFrom(cls)) {
                    System.out.println("true");
                }
                if (Map.class.isAssignableFrom(cls)) {
                    System.out.println("true");
                }

                Map<String, Integer> map = new HashMap<>();
                map.put("HH1", 1);
                map.put("HH2", 2);
                System.out.println(JsonUtil.toJSON(map));
            }
        }
        System.out.println("OftenUtil==================================================");
        {
            {
                String[] arr = StringUtils.split("1aa,b，c", "a,");
                System.out.println(JsonUtil.toJSON(arr));// ["1","b，c"]
            }
            {
                String[] arr = StringUtils.splitByWholeSeparator("1aa,b，c", "a,");
                assert arr.length == 2;
                System.out.println(JsonUtil.toJSON(arr));// ["1a","b，c"]
            }
            {
                String[] arr = StringUtils.splitByWholeSeparator("1aa,", "1aa,");
                assert arr.length == 1;
                System.out.println(JsonUtil.toJSON(arr));// ["1a","b，c"]
            }
            {
                List<String> ls = StringUtil.splitByWholeSeparators("1,2,3，4678,67", ",", "，", "67");
                assert ls.size() == 5;
                System.out.println(JsonUtil.toJSON(ls));
            }
            {
                List<String> ls = StringUtil.splitByWholeSeparators("6767", "67");
                assert ls.size() == 0;
                System.out.println(JsonUtil.toJSON(ls));
            }
            {
                List<String> ls = StringUtil.splitByWholeSeparators("67 67 6", "67");
                assert ls.size() == 1;
                System.out.println(JsonUtil.toJSON(ls));
            }
        }
        System.out.println("OftenUtil==================================================");
        {
            {
                String cut = OftenUtil.StringUtil.cut("1", 1);
                System.out.println(cut);
                cut = OftenUtil.StringUtil.cut("12", 3);
                System.out.println(cut);
                String hxy = OftenUtil.StringUtil.lowerFirstChar("HXY");
                System.out.println(hxy);
            }
            {
                String padLeft = StringUtil.padLeft("1", 3, '0');
                System.out.println(padLeft);
                String padRight = StringUtil.padRight("1", 3, '0');
                System.out.println(padRight);
            }
            {
                boolean r = OftenUtil.CompareUtil.isEquals((String) null, null);
                System.out.println(r);
                r = OftenUtil.CompareUtil.isEquals((String) null, "");
                System.out.println(r);
            }
        }
        System.out.println("Optional==================================================");
        {
            List<String> ls = null;
            List<String> ols = Optional.ofNullable(ls)
                    .orElse(new ArrayList<>());
            System.out.println(ols);

            List<String> ss = Arrays.asList("1", "2");
            ols = Optional.ofNullable(ls)
                    .orElse(new ArrayList<>(ss));
            System.out.println(ols);

            ls = new ArrayList<>(ss);
            ols = Optional.ofNullable(ls)
                    .orElse(new ArrayList<>());
            System.out.println(ols);
        }
        System.out.println("MethodSignature==================================================");
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
            System.out.println("==================================================");
            // HttpTxtParser
            {
                String filePath = "D:\\OD\\OneDrive\\_Now\\Wode\\.http";
                File file = new File(filePath);
                HttpTxtParser httpTxtParser = new HttpTxtParser(file);
                List<HttpTxtParser.HttpTxtInfo> ls = httpTxtParser.parse();
                for (HttpTxtParser.HttpTxtInfo item : ls) {
                    System.out.println(JsonUtil.toJSON(item));
                }

                // 序列化为文件
                String str = HttpTxtParser.writeTo(ls);
                System.out.println(str);
            }
            System.out.println("==================================================");
            // 获取硬件信息，慢！！！
            {
                List<InetAddress> ls = NetUtil.getIPV4InetAddress();
                System.out.println(ls);
                List<InetAddress> ls1 = NetUtil.getIPV4InetAddress("Intel");
                System.out.println(ls1);
                String localIP = NetUtil.getLocalIP();
                System.out.println(localIP);
            }
            System.out.println("==================================================");
            //
        }
    }

}
