package com.hxx.sbConsole.service.impl.demo;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hxx.sbConsole.jwt.JWTUtil;
import com.hxx.sbConsole.model.Person;
import com.hxx.sbConsole.model.User;
import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.array.CollectionUtil;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.hardware.NetUtil;
import com.hxx.sbcommon.common.io.fileOrDir.PathUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;
import com.hxx.sbcommon.common.other.pageSeparate.GeneralPageable;
import com.hxx.sbcommon.common.other.pageSeparate.PageSeparate;
import com.hxx.sbcommon.common.other.pageSeparate.Pageable;
import com.hxx.sbcommon.common.reflect.MethodSignature;
import com.hxx.sbcommon.model.Result;
import lombok.var;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StopWatch;

import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:17:53
 **/
@var
@Service
public class DemoService {
    public static void main(String[] args) {
        try {
            {

            }
            {
                int dv = 0B10110011;
                byte[] data = Integer.toBinaryString(dv).getBytes(StandardCharsets.ISO_8859_1);
//                long ccittCrc = CRC.calculateCRC(CRC.Parameters.CCITT, data);
//                System.out.printf("CRC is 0x%04X\n", ccittCrc); // prints "CRC is 0x29B1"
            }
            {
                String str1 = "2/1/3";
                str1 = str1.replace('/', '-');
                System.out.println(str1);
            }
            {
                User user = new User();
                List<Integer> ls = Optional.ofNullable(user).map(d -> d.getLs()).orElse(new ArrayList<>());
                System.out.println(ls);
            }
            {
                List<String> list = new ArrayList<>(Arrays.asList("1", "2", "3", "jay"));
                list.remove("2");

                CollectionUtil.remove(list, d -> "jay".equals(d));
                System.out.println(list);
            }
            {
                Collection<String> src = Arrays.asList("1", "2", "3");
                Collection<String> tar = Arrays.asList("1", "2", "4");
                Collection<String> result = CollectionUtil.subtract(src, tar);
                System.out.println(result);// 3
            }
            {
                Result<Person> result = Result.success(null);
                Long personId = Optional.ofNullable(result)
                        .map(d -> d.isState() ? d.getData() : null)
                        .map(d -> d.getId())
                        .orElse(-999L);
                System.out.println(personId);
            }
            {
                String str = null;

                String oe = Optional.ofNullable(str)
                        .map(d -> d.trim())
                        .map(d -> d.toLowerCase())
                        .orElse("我是默认值");
                System.out.println(oe);

                OftenUtil.proc(str, d -> d.toLowerCase(), "1111");
                String s = OftenUtil.proc(str, d -> d.trim(), "1111");
                System.out.println(s);
            }
            {
                var path = "d:/tmp/11";
                boolean flag = PathUtil.checkAndMkDirs(path);
                System.out.println(flag);
            }
            {
                Long a = 999L;
                Object b = 999;
                System.out.println(OftenUtil.equals(a, b));

                String s1 = StringUtils.substring("1234567890", 0, 100);
                System.out.println(s1);
            }
            {
                LocalDateTime modifiedOn = LocalDateTime.now().minusDays(3);
                System.out.println(modifiedOn);
            }
            {
                StopWatch sw = new StopWatch();
                sw.start();
                Thread.sleep(800);
                if (sw.isRunning()) sw.stop();
                if (sw.isRunning()) sw.stop();
                System.out.println("cost:" + sw.getTotalTimeMillis());
            }
            {
                Function<String, String> toUpperCase = str -> str.toUpperCase();
                Function<String, String> multiplyByTwo = num -> num + "_" + num;

                Function<String, String> doubleEachChar = toUpperCase.andThen(multiplyByTwo);
                String result = "hello world";
                String apply = doubleEachChar.apply(result);
                System.out.println(apply);
            }
            {
                float num = new BigDecimal("45.6789614").floatValue();
                String str = OftenUtil.NumberUtil.fmt2Str(num);
                System.out.println(str);
            }
            {
                // split 这种情况下会有空项
                String[] arr = "&&1".split("&&");
                System.out.println(arr);
            }
            List<String> strls = new ArrayList<>();
            Map<String, Integer> strlMap = strls.stream().collect(Collectors.toMap(d -> d, d -> d.length()));

            Set<String> set1 = new HashSet<>();
            String str1 = null;
            System.out.println(set1.contains(str1));
            Map<String, Integer> map1 = new HashMap<>();
            System.out.println(map1.containsKey(str1));

            List<String> ls = null;
            List<String> ls1 = Optional.ofNullable(ls).orElse(new ArrayList<>());

            Integer integer1 = NumberUtils.toInt("987654", 0);
            int integer2 = 987654;
            boolean flag = Objects.equals(integer2, integer1);

            LocalDateTime begin = OftenUtil.DateTimeUtil.parseDateTime("2023-07-01 00:00:00");
            LocalDateTime end = OftenUtil.DateTimeUtil.parseDateTime("2023-07-07 23:59:59");
            long days = Duration.between(begin, end).toDays();// 6
            System.out.println(days);

            begin = OftenUtil.DateTimeUtil.parseDateTime("2023-07-01 00:00:00");
            end = OftenUtil.DateTimeUtil.parseDateTime("2023-07-01 23:59:59");
            days = Duration.between(begin, end).toDays();// 0
            System.out.println(days);

            // String... 需要判断null
            argArr();
            argArr(null);
            argArr("");

            File tempFile = new File(System.currentTimeMillis() + ".xls");
            tempFile.delete();// 文件不存在，也可以直接删除

            File mdFile = ResourceUtils.getFile("classpath:demo/Markdown示例.md");
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void demo() throws Exception {
        System.out.println("BasicType==================================================");
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
            String s = OftenUtil.proc(item, String::trim, "");
            item = " 98 ";
            s = OftenUtil.proc(item, String::trim, "");

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
        System.out.println("OftenUtil.StringUtil==================================================");
        {
            {
                String str = "12a3456q09874560";
                String str1 = StringUtil.getEndNumber(str);
                System.out.println(str1);
            }
            {
                {
                    String cut = StringUtil.cut("1", 1);
                    System.out.println(cut);
                }
                {
                    String cut = StringUtil.cut("ab", 1);
                    System.out.println(cut);
                }
                {
                    String cut = StringUtil.cut("abc", 2);
                    System.out.println(cut);
                }
                {
                    String cut = StringUtil.cut("12", 3);
                    System.out.println(cut);
                }
                String hxy = StringUtil.lowerFirstChar("HXY");
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
            {
                List<String> ls = new ArrayList<>();
                ls.add("0");
                ls.add(null);
                ls.add("");
                ls.add("1");
                Set<String> arr1 = CollectionUtil.getFieldSet(ls, d -> d, d -> true);
                System.out.println("arr1:" + JsonUtil.toJSON(arr1));
                Set<String> arr2 = CollectionUtil.getFieldSet(ls, d -> d, d -> d != null);
                System.out.println("arr2:" + JsonUtil.toJSON(arr2));
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
//            // HttpTxtParser
//            {
//                String filePath = "D:\\OD\\OneDrive\\_Now\\Wode\\.http";
//                File file = new File(filePath);
//                HttpTxtParser httpTxtParser = new HttpTxtParser(file);
//                List<HttpTxtParser.HttpTxtInfo> ls = httpTxtParser.parse();
//                for (HttpTxtParser.HttpTxtInfo item : ls) {
//                    System.out.println(JsonUtil.toJSON(item));
//                }
//
//                // 序列化为文件
//                String str = HttpTxtParser.writeTo(ls);
//                System.out.println(str);
//            }
            System.out.println("==================================================");
            // 获取硬件信息，慢！！！
            {
                InetAddress ipv4 = NetUtil.getIPV4();
                System.out.println(ipv4);
                String localMac = NetUtil.getLocalMac();
                System.out.println(localMac);

                Map<String, String> allLocalMacs = NetUtil.getAllLocalMacs();
                System.out.println(JsonUtil.toJSON(allLocalMacs));

                List<InetAddress> ls = NetUtil.getIPV4InetAddress();
                System.out.println(ls);
                List<InetAddress> ls1 = NetUtil.getIPV4InetAddress(Arrays.asList("Intel"));
                System.out.println(ls1);
                String localIP = NetUtil.getLocalIP();
                System.out.println(localIP);
            }
            System.out.println("==================================================");
            //
        }
    }

    public static void argArr(String... args) {
        if (args == null) {
            System.out.println("args = null");
            return;
        }
        System.out.println("args = " + args);
    }

}
