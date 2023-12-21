package com.hxx.sbcommon.common.basic;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 开发常用工具类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-06 10:49:59
 **/
public class OftenUtil {

    /**
     * 评估条件，为true 抛出不通过异常
     *
     * @param condition
     */
    public static void assertCond(boolean condition) {
        if (condition) {
            throw new IllegalArgumentException("评估条件不通过");
        }
    }

    /**
     * 评估条件，为true，抛出 IllegalArgumentException
     *
     * @param condition 条件
     * @param errMsg
     */
    public static void assertCond(boolean condition, String errMsg) {
        if (condition) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * 评估条件，为true，抛出 IllegalArgumentException，支持slf4j的方式格式化消息
     *
     * @param condition
     * @param errMsg
     * @param args
     */
    public static void assertCond(boolean condition, String errMsg, Object... args) {
        if (condition) {
            if (args == null || args.length == 0) {
                throw new IllegalArgumentException(errMsg);
            }
            FormattingTuple fmtTup = MessageFormatter.arrayFormat(errMsg, args);
            throw new IllegalArgumentException(fmtTup.getMessage());
        }
    }

    /**
     * 评估条件，为true，则响应指定异常
     *
     * @param condition
     * @param ex
     */
    public static <T extends RuntimeException> void assertCond(boolean condition, T ex) {
        if (condition) {
            throw ex;
        }
    }

    /**
     * 基本类型相等性比较
     * 常用基本类型：整型（byte、short、int、long）、浮点型（float、double）、布尔（boolean）、字符（char）
     *
     * @param obj
     * @param obj2
     * @return
     */
    public static boolean equals(Object obj, Object obj2) {
        // Objects.equals(a, b);
        // return (a == b) || (a != null && a.equals(b));
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        // 如果两个类型一致，则直接比较
        {
            if (obj instanceof Byte && obj2 instanceof Byte) {
                return obj.equals(obj2);
            }
            if (obj instanceof Short && obj2 instanceof Short) {
                return obj.equals(obj2);
            }
            if (obj instanceof Integer && obj2 instanceof Integer) {
                return obj.equals(obj2);
            }
            if (obj instanceof Long && obj2 instanceof Long) {
                return obj.equals(obj2);
            }

            if (obj instanceof Float && obj2 instanceof Float) {
                return obj.equals(obj2);
            }
            if (obj instanceof Double && obj2 instanceof Double) {
                return obj.equals(obj2);
            }

            if (obj instanceof Boolean && obj2 instanceof Boolean) {
                return obj.equals(obj2);
            }

            if (obj instanceof Character && obj2 instanceof Character) {
                return obj.equals(obj2);
            }

            if (obj.getClass() == obj2.getClass()) {
                return obj.equals(obj2);
            }
        }

        // 类型不一致时，直接转为兼容类型后比较
        {
            Object objVal = castVal(obj);
            Object obj2Val = castVal(obj2);
            return objVal.equals(obj2Val);
        }
    }

    // log

    /**
     * 每10分钟时写日志
     *
     * @param log
     * @param logAct
     */
    public static void everyTenMinuteLog(org.slf4j.Logger log, Consumer<org.slf4j.Logger> logAct) {
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        int second = now.getSecond();
        if (minute % 10 == 0 && second > 50) {
            logAct.accept(log);
        }
    }

    // 校验

    /**
     * 字符是否null或空白字符
     *
     * @param ch
     * @return
     */
    public static boolean isNullOrWhiteSpace(Character ch) {
        return ch == null || ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '\f' || ch == '\b';
//        return StringUtils.isBlank(ch);
//        return Character.isWhitespace(ch);
    }

    /**
     * 校验包装类型有效性，
     * 注意：会要求入参大于0
     *
     * @param i
     * @return
     */
    public static boolean validInteger(Integer i) {
        return i != null && i > 0;
    }

    /**
     * 校验日期有效性
     * 注意：年份必须大于2000年
     *
     * @param ldt
     * @return
     */
    public static boolean validLocalDateTime(LocalDateTime ldt) {
        return ldt != null && ldt.getYear() > 2000;
    }

    // 通用操作

    /**
     * 通用处理数据，为null时使用默认值
     *
     * @param source
     * @param mapperFunc
     * @param defaultVal
     * @param <T>
     * @param <F>
     * @return
     */
    public static <T, F> F proc(T source, Function<T, F> mapperFunc, F defaultVal) {
        return Optional.ofNullable(source)
                .map(mapperFunc)
                .orElse(defaultVal);
    }

    // UUID

    /**
     * 获取GUID，不带-
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    // 字节与HEX

    /**
     * 字符串转HEX字符串
     *
     * @param str      输入字符串
     * @param splitStr HEX字符串的连接字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toHexString(String str, String splitStr) throws UnsupportedEncodingException {
        return toHexString(str, "UTF-8", splitStr);
    }

    /**
     * 字符串转HEX字符串
     *
     * @param str         输入字符串
     * @param charsetName 字符集名称，GBK、UTF-8
     * @param splitStr    HEX字符串的连接字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toHexString(String str, String charsetName, String splitStr) throws UnsupportedEncodingException {
        byte[] arr = str.getBytes(charsetName);
        return toHexString(arr, splitStr);
    }

    /**
     * 字节数组转HEX字符串
     *
     * @param arr
     * @param splitStr
     * @return
     */
    public static String toHexString(byte[] arr, String splitStr) {
        splitStr = splitStr == null ? "" : splitStr;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            byte b = arr[i];
            String str1 = Integer.toHexString(b & 0xFF);
            sb.append(str1);
            if (i != arr.length - 1) {
                sb.append(splitStr);
            }
        }

        return sb.toString();
    }

    /**
     * 权重随机
     *
     * @param map k=服务，v=权重
     * @return
     */
    public static String weightRandom(Map<String, Integer> map) {
        int seed = 0;
        List<String> ls = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            // 权重
            Integer weight = entry.getValue();
            seed += weight;
            for (int i = 0; i < weight; i++) {
                ls.add(entry.getKey());
            }
        }

        Collections.sort(ls);

        java.util.Random r = new java.util.Random();
        int randomVal = r.nextInt(seed);
        return ls.get(randomVal);
    }


    static Object castVal(Object obj) {
        if (obj instanceof Byte) {
            return ((Byte) obj).longValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).longValue();
        }
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
//        if (obj instanceof Long) {
//            return obj;
//        }

        if (obj instanceof Float) {
            return ((Float) obj).doubleValue();
        }
//        if (obj instanceof Double) {
//            return obj;
//        }
//
//        if (obj instanceof Boolean) {
//            return obj;
//        }
//
//        if (obj instanceof Character) {
//            return obj;
//        }
        return obj;
    }

    // 基础字段处理
    public static class BasicUtil {

    }

    // 数字
    public static class NumberUtil {
        /**
         * 基础类型相等比较，
         * 仅支持Java的8大基础类型（Short、Integer、Long、Float、Double、Byte、Boolean、Character）
         * 以及BigDecimal、String之间互相比较
         * 注意：整型之间和浮点型之间可以互相比较
         *
         * @param obj
         * @param eqObj
         * @return
         */
        public static boolean basicEquals(Object obj, Object eqObj) {
            if (obj == null) return eqObj == null;

            // 字符串
            if (obj instanceof String) {
                if (eqObj == null) return false;

                if (eqObj instanceof String) return obj.equals(eqObj);
            }
            // 布尔
            else if (obj instanceof Boolean) {
                if (eqObj == null) return false;

                boolean objVal = (Boolean) obj;
                if (eqObj instanceof Boolean) return objVal == (Boolean) eqObj;
            }
            // 字符
            else if (obj instanceof Character) {
                if (eqObj == null) return false;

                char objVal = (Character) obj;
                if (eqObj instanceof Character) return objVal == (Character) eqObj;
            }
            // 字节
            else if (obj instanceof Byte) {
                if (eqObj == null) return false;

                byte objVal = (Byte) obj;
                if (eqObj instanceof Integer) return objVal == ((Integer) eqObj).byteValue();
                if (eqObj instanceof Short) return objVal == ((Short) eqObj).byteValue();
                if (eqObj instanceof Long) return objVal == ((Long) eqObj).byteValue();

//                if (eqObj instanceof Float) return obj.equals(((Float) eqObj).byteValue());
//                if (eqObj instanceof Double) return obj.equals(((Double) eqObj).byteValue());
//                if (eqObj instanceof BigDecimal) return obj.equals(((BigDecimal) eqObj).byteValue());

                if (eqObj instanceof Byte) return objVal == (Byte) eqObj;
            }
            // 整型
            else if (obj instanceof Short) {
                if (eqObj == null) return false;

                short objVal = (Short) obj;
                if (eqObj instanceof Integer) return objVal == ((Integer) eqObj).shortValue();
                if (eqObj instanceof Short) return objVal == (Short) eqObj;
                if (eqObj instanceof Long) return objVal == ((Long) eqObj).shortValue();

//                if (eqObj instanceof Float) return obj.equals(((Float) eqObj).shortValue());
//                if (eqObj instanceof Double) return obj.equals(((Double) eqObj).shortValue());
//                if (eqObj instanceof BigDecimal) return obj.equals(((BigDecimal) eqObj).shortValue());

                if (eqObj instanceof Byte) return objVal == ((Byte) eqObj).shortValue();
            } else if (obj instanceof Integer) {
                if (eqObj == null) return false;

                int objVal = (Integer) obj;
                if (eqObj instanceof Integer) return objVal == (Integer) eqObj;
                if (eqObj instanceof Short) return objVal == ((Short) eqObj).intValue();
                if (eqObj instanceof Long) return objVal == ((Long) eqObj).intValue();

//                if (eqObj instanceof Float) return obj.equals(((Float) eqObj).intValue());
//                if (eqObj instanceof Double) return obj.equals(((Double) eqObj).intValue());
//                if (eqObj instanceof BigDecimal) return obj.equals(((BigDecimal) eqObj).intValue());

                if (eqObj instanceof Byte) return objVal == ((Byte) eqObj).intValue();
            } else if (obj instanceof Long) {
                if (eqObj == null) return false;

                long objVal = (Long) obj;
                if (eqObj instanceof Integer) return objVal == ((Integer) eqObj).longValue();
                if (eqObj instanceof Short) return objVal == ((Short) eqObj).longValue();
                if (eqObj instanceof Long) return objVal == (Long) eqObj;

//                if (eqObj instanceof Float) return obj.equals(((Float) eqObj).longValue());
//                if (eqObj instanceof Double) return obj.equals(((Double) eqObj).longValue());
//                if (eqObj instanceof BigDecimal) return obj.equals(((BigDecimal) eqObj).longValue());

                if (eqObj instanceof Byte) return objVal == ((Byte) eqObj).longValue();
            }
            // 浮点数
            else if (obj instanceof Float) {
                if (eqObj == null) return false;

//                if (eqObj instanceof Integer) return obj.equals(((Integer) eqObj).floatValue());
//                if (eqObj instanceof Short) return obj.equals(((Short) eqObj).floatValue());
//                if (eqObj instanceof Long) return obj.equals(((Long) eqObj).floatValue());

                float objVal = (Float) obj;
                if (eqObj instanceof Float) return objVal == (Float) eqObj;
                if (eqObj instanceof Double) return objVal == ((Double) eqObj).floatValue();
                if (eqObj instanceof BigDecimal) return objVal == ((BigDecimal) eqObj).floatValue();

//                if (eqObj instanceof Byte) return obj.equals(((Byte) eqObj).floatValue());
            } else if (obj instanceof Double) {
                if (eqObj == null) return false;

//                if (eqObj instanceof Integer) return obj.equals(((Integer) eqObj).doubleValue());
//                if (eqObj instanceof Short) return obj.equals(((Short) eqObj).doubleValue());
//                if (eqObj instanceof Long) return obj.equals(((Long) eqObj).doubleValue());

                double objVal = (Double) obj;
                if (eqObj instanceof Float) return objVal == ((Float) eqObj).doubleValue();
                if (eqObj instanceof Double) return objVal == (Double) eqObj;
                if (eqObj instanceof BigDecimal) return objVal == ((BigDecimal) eqObj).doubleValue();

//                if (eqObj instanceof Byte) return obj.equals(((Byte) eqObj).doubleValue());
            } else if (obj instanceof BigDecimal) {
                if (eqObj == null) return false;

//                if (eqObj instanceof Integer) return obj.equals(BigDecimal.valueOf((Integer) eqObj));
//                if (eqObj instanceof Short) return obj.equals(BigDecimal.valueOf((Short) eqObj));
//                if (eqObj instanceof Long) return obj.equals(BigDecimal.valueOf((Long) eqObj));

                BigDecimal objVal = (BigDecimal) obj;
                if (eqObj instanceof Float) return objVal.equals(BigDecimal.valueOf((Float) eqObj));
                if (eqObj instanceof Double) return objVal.equals(BigDecimal.valueOf((Double) eqObj));
                if (eqObj instanceof BigDecimal) return objVal.equals(eqObj);

//                if (eqObj instanceof Byte) return obj.equals(BigDecimal.valueOf((Byte) eqObj));
            }
            throw new IllegalArgumentException("不支持的类型比较");
        }

        /**
         * 格式化为字符串，
         * 为null时返回空字符串，
         * 去除1.00后的无意义0
         *
         * @param val
         * @return
         */
        public static String fmt2Str(Float val) {
            return fmt2Str(val == null ? null : val.doubleValue());
        }

        /**
         * 格式化为字符串，
         * 为null时返回空字符串，
         * 去除1.00后的无意义0
         *
         * @param val
         * @return
         */
        public static String fmt2Str(Double val) {
            if (val == null) {
                return "";
            }

            if (val == 0) {
                return "0";
            }

            // 去除小数点后全是零的情况
            if (val == val.intValue()) {
                return val.intValue() + "";
            }

            return fmt2Str(new BigDecimal(val));
        }

        /**
         * 格式化为字符串，
         * 为null时返回空字符串，
         * 去除1.00后的无意义0
         * 不以科学计数法表示
         *
         * @param val
         * @return
         */
        public static String fmt2Str(BigDecimal val) {
            if (val == null) {
                return "";
            }

            if (val.compareTo(BigDecimal.ZERO) == 0) {
                return "0";
            }

            // 去除小数点后无意义零,
            return val.stripTrailingZeros()
                    //且不以科学计数法表示
                    .toPlainString();
        }

        /**
         * 字符串转为BigDecimal
         * BigDecimal能够精确地表示你希望的数值，一定要使用字符串来表示小数，并传递给BigDecimal的构造函数
         *
         * @param num
         * @return
         */
        public static BigDecimal convert2BigDecimal(String num) {
            return new BigDecimal(num);
        }

        /**
         * 分子与分母相除后保留指定位数
         * 四舍五入
         *
         * @param numerator   分子
         * @param denominator 分母
         * @param scale       保留精度
         * @return
         */
        public static BigDecimal divide(BigDecimal numerator, BigDecimal denominator, int scale) {
            if (null == numerator || null == denominator || numerator.compareTo(BigDecimal.ZERO) == 0 || denominator.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.valueOf(0);
            }

            // 4舍，5入，其他未测
            return numerator.divide(denominator, scale, RoundingMode.HALF_UP);
        }

        /**
         * 分子乘以100后，与分母相除后保留指定位数
         * 四舍五入
         *
         * @param numerator   分子
         * @param denominator 分母
         * @param scale       保留精度
         * @return
         */
        public static BigDecimal roundPercent(BigDecimal numerator, BigDecimal denominator, int scale) {
            if (null == numerator || null == denominator || numerator.compareTo(BigDecimal.ZERO) == 0 || denominator.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.valueOf(0);
            }

            BigDecimal decimal = numerator.multiply(new BigDecimal(100));
            return divide(decimal, denominator, scale);
        }
    }

    // 日期
    public static class DateTimeUtil {
        // yyyy-MM-dd HH:mm:ss
        private final static DateTimeFormatter DateTime_Default_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // yyyy-MM-dd
        private final static DateTimeFormatter DateTime_Date_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /**
         * 格式化为：yyyy-MM-dd HH:mm:ss
         *
         * @param dateTime
         * @return
         */
        public static String fmt2Str(LocalDateTime dateTime) {
            if (dateTime == null) return "";
            return dateTime.format(DateTime_Default_Formatter);
        }

        /**
         * 格式化为：yyyy-MM-dd HH:mm:ss
         *
         * @param dateTime
         * @return
         */
        public static String fmt2Str(Date dateTime) {
            if (dateTime == null) return "";
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return f.format(dateTime);
        }

        /**
         * 格式化为：yyyy-MM-dd
         *
         * @param dateTime
         * @return
         */
        public static String fmt2DateStr(LocalDateTime dateTime) {
            if (dateTime == null) return "";
            return dateTime.format(DateTime_Date_Formatter);
        }

        /**
         * 格式化为：yyyy-MM-dd
         *
         * @param dateTime
         * @return
         */
        public static String fmt2DateStr(Date dateTime) {
            if (dateTime == null) return "";
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return f.format(dateTime);
        }

        /**
         * 解析为LDT，支持字符串格式：yyyy-MM-dd HH:mm:ss
         *
         * @param text
         * @return
         */
        public static LocalDateTime parseDateTime(String text) {
            return LocalDateTime.parse(text, DateTime_Default_Formatter);
        }

        /**
         * 解析为LDT，支持字符串格式：yyyy-MM-dd HH:mm:ss
         *
         * @param text
         * @param defaultVal
         * @return
         */
        public static LocalDateTime parseDateTime(String text, LocalDateTime defaultVal) {
            if (StringUtils.isBlank(text)) return defaultVal;

            try {
                return LocalDateTime.parse(text, DateTime_Default_Formatter);
            } catch (Exception ignore) {
                return defaultVal;
            }
        }

        /**
         * 解析为LDT，支持字符串格式：yyyy-MM-dd
         *
         * @param text
         * @return
         */
        public static LocalDateTime parseDate(String text) {
            return LocalDateTime.parse(text, DateTime_Date_Formatter);
        }

        /**
         * 时间戳转LDT
         *
         * @param timestamp 转13位毫秒时间戳
         * @return
         */
        public static LocalDateTime parse(long timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        }

        /**
         * 转13位毫秒时间戳
         *
         * @param dt
         * @return
         */
        public static long toTimestamp(LocalDateTime dt) {
            return dt.toInstant(ZoneOffset.ofHours(8))
                    .toEpochMilli();
        }

        /**
         * Date转LDT
         *
         * @param date Date
         * @return
         */
        public static LocalDateTime parse(Date date) {
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        }

        /**
         * 转Date
         *
         * @param dt
         * @return
         */
        public static Date toDate(LocalDateTime dt) {
            return Date.from(dt.atZone(ZoneId.systemDefault())
                    .toInstant());
        }

        /**
         * 验证时间段是否超过指定最大天数
         *
         * @param begin
         * @param end
         * @param maxDay
         * @return
         */
        public static boolean check(LocalDateTime begin, LocalDateTime end, int maxDay) {
            Duration dur = Duration.between(begin, end);
            return dur.getSeconds() < maxDay * 24 * 60 * 60L;
        }

        // 日期计算

        /**
         * 获取开始结束时间间隔小时数
         *
         * @param startTime
         * @param endTime
         * @return
         */
        public static long getIntervalHour(LocalDateTime startTime, LocalDateTime endTime) {
            return ChronoUnit.HOURS.between(startTime, endTime);
        }

        /**
         * 获取两个时间间隔的天数，必须是精确到秒级的时间段
         * 使用时>=start && <=end
         *
         * @param start 开始时间
         * @param end   结束时间 必须带上23：59：59
         * @return
         */
        public static Long getIntervalDays(Date start, Date end) {
            // 这样得到的差值是微秒级别
            long diff = end.getTime() + 1000 - start.getTime();

            int dayMs = 1000 * 60 * 60 * 24;
            // 间隔天数
            long days = diff / dayMs;
            if (diff % dayMs != 0) {
                days++;
            }

            return days;
        }
    }

    // 集合
    public static class CollectionUtil {

        /**
         * 有且只有一个
         *
         * @param ls
         * @param predicate
         * @param <T>
         * @return
         */
        public static <T> T single(List<T> ls, Predicate<T> predicate) {
            if (CollectionUtils.isEmpty(ls)) {
                throw new IllegalStateException("集合不包含任何元素");
            }

            long count = ls.stream()
                    .filter(predicate)
                    .count();
            if (count > 1) {
                throw new IllegalStateException("集合包含多个符合条件的元素");
            }

            return ls.stream()
                    .filter(predicate)
                    .findFirst()
                    .orElse(null);
        }

        /**
         * 有且只有一个
         *
         * @param ls
         * @param <T>
         * @return
         */
        public static <T> T single(List<T> ls) {
            if (CollectionUtils.isEmpty(ls)) {
                throw new IllegalStateException("集合不包含任何元素");
            } else {
                if (ls.size() > 1) {
                    throw new IllegalStateException("集合包含多个元素");
                }

                return ls.get(0);
            }
        }

        /**
         * 有且不少于1个符合条件的元素
         *
         * @param ls
         * @param predicate
         * @param <T>
         * @return
         * @throws IllegalStateException
         */
        public static <T> T first(List<T> ls, Predicate<T> predicate) {
            if (CollectionUtils.isEmpty(ls)) {
                throw new IllegalStateException("集合不包含任何元素");
            }

            T ele = ls.stream()
                    .filter(predicate)
                    .findFirst()
                    .orElse(null);
            if (ele == null) {
                throw new IllegalStateException("未在集合中找到符合条件的元素");
            }
            return ele;
        }

        /**
         * 有且不少于1个
         *
         * @param ls
         * @param <T>
         * @return
         */
        public static <T> T first(List<T> ls) {
            if (CollectionUtils.isEmpty(ls)) {
                throw new IllegalStateException("集合不包含任何元素");
            }
            return ls.get(0);
        }

        /**
         * 取第一个符合条件的元素，不存在取默认值
         *
         * @param ls
         * @param predicate
         * @param defaultVal
         * @param <T>
         * @return
         * @throws Exception
         */
        public static <T> T firstOrDefault(List<T> ls, Predicate<T> predicate, T defaultVal) {
            if (CollectionUtils.isEmpty(ls)) {
                return defaultVal;
            }
            return ls.stream()
                    .filter(predicate)
                    .findFirst()
                    .orElse(defaultVal);
        }

        /**
         * 取第一个元素，不存在取默认值
         *
         * @param ls
         * @param defaultVal
         * @param <T>
         * @return
         */
        public static <T> T firstOrDefault(List<T> ls, T defaultVal) {
            return CollectionUtils.isEmpty(ls) ? defaultVal : ls.get(0);
        }

        /**
         * 简单缓存简化方法
         *
         * @param cache
         * @param key
         * @param getCacheValFunc
         * @param <TKey>
         * @param <TVal>
         * @return
         */
        public static <TKey, TVal> TVal getCacheVal(Map<TKey, TVal> cache, TKey key, Function<TKey, TVal> getCacheValFunc) {
            TVal val = cache.getOrDefault(key, null);
            if (val == null) {
                val = getCacheValFunc.apply(key);
                cache.put(key, val);
            }
            return val;
        }
    }

    // 随机数
    public static class RandomUtil {
        /**
         * 获取随机数
         * 不能在多线程之间共享ThreadLocalRandom
         *
         * @param origin 起始
         * @param bound  界限
         * @return
         */
        public static int nextRandomVal(int origin, int bound) {
            int randomVal = ThreadLocalRandom.current()
                    .nextInt(origin, bound);
            return randomVal;
        }

        /**
         * 获取随机数
         * 不能在多线程之间共享ThreadLocalRandom
         *
         * @param bound 界限
         * @return
         */
        public static int nextRandomVal(int bound) {
            int randomVal = ThreadLocalRandom.current()
                    .nextInt(0, bound);
            return randomVal;
        }
    }

    // 对比
    public static class CompareUtil {
        /**
         * 字符串是否相等,空字符串、null、空白字符串默认相等
         *
         * @param v1
         * @param v2
         * @return
         */
        public static boolean isEquals(String v1, String v2) {
            if (StringUtils.isBlank(v1) && StringUtils.isBlank(v2)) {
                return true;
            }
            if (StringUtils.isBlank(v1) || StringUtils.isBlank(v2)) {
                return false;
            }

            return v1.trim()
                    .equals(v2.trim());
        }

        /**
         * 整型是否相等
         *
         * @param v1
         * @param v2
         * @return
         */
        public static boolean isEquals(Integer v1, Integer v2) {
            if (v1 == null && v2 == null) {
                return true;
            }
            if (v1 == null || v2 == null) {
                return false;
            }
            return v1.intValue() == v2.intValue();
        }

        /**
         * Byte是否相等
         *
         * @param v1
         * @param v2
         * @return
         */
        public static boolean isEquals(Byte v1, Byte v2) {
            if (v1 == null && v2 == null) {
                return true;
            }
            if (v1 == null || v2 == null) {
                return false;
            }
            return v1.byteValue() == v2.byteValue();
        }

    }

    // 数据库
    public static class DBUtil {
        /**
         * 获取mysql的limit左值
         *
         * @param pageNum  页码，从1开始
         * @param pageSize 页大小
         * @return
         */
        public static int getMySqlLimitLeftVal(int pageNum, int pageSize) {
            return (pageNum - 1) * pageSize;
        }
    }
}
