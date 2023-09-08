package com.hxx.sbcommon.common.basic;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
     * 字段为null时设置默认值
     * 适合常见类型和集合
     * 如果为字符串，则去除空格
     *
     * @param source       模型
     * @param getFieldFunc 字段
     * @param defaultVal   字段为null时的默认值
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> U getNullToDefault(T source, Function<T, U> getFieldFunc, U defaultVal) {
        U fieldVal = getFieldFunc.apply(source);
        if (fieldVal == null) {
            return defaultVal;
        }

        if (defaultVal instanceof String) {
            return (U) ((String) fieldVal).trim();
        }

        return fieldVal;
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
     * 调用指定方法，并返回值，
     * 如果抛出异常，则返回null
     *
     * @param t
     * @param act
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R tryRun(T t, Function<T, R> act) {
        try {
            return act.apply(t);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 调用并忘记
     *
     * @param t
     * @param act
     * @param <T>
     */
    public static <T> void tryRun(T t, Consumer<T> act) {
        try {
            act.accept(t);
        } catch (Exception ignored) {
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
     * 查询实体中字段的值，支持实体为null
     *
     * @param t
     * @param func
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R getFieldVal(T t, Function<T, R> func) {
        if (t == null) return null;
        return Arrays.asList(t).stream()
                .map(func)
                .findFirst()
                .orElse(null);
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
        return toHexString(arr, charsetName, splitStr);
    }

    /**
     * 字节数组转HEX字符串
     *
     * @param arr
     * @param charsetName
     * @param splitStr
     * @return
     */
    public static String toHexString(byte[] arr, String charsetName, String splitStr) {
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

    // 数字
    public static class NumberUtil {
        /**
         * 格式化为字符串，
         * 为null时返回空字符串，
         * 去除1.00后的无意义0
         *
         * @param val
         * @return
         */
        public static String fmt2Str(Float val) {
            Double dval = null;
            if (val != null) {
                dval = val.doubleValue();
            }

            return fmt2Str(dval);
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

    // 字符串
    public static class StringUtil {
        /**
         * 取字符串的前n位,
         * 超过字符串总长度不会报错
         *
         * @param str
         * @param count
         * @return
         */
        public static String cut(String str, int count) {
            if (StringUtils.isEmpty(str)) return str;
            if (count == 0) return "";

            int len = str.length();
            if (count >= len) {
                return str;
            }

            return str.substring(0, count);
        }

        /**
         * 小写第一位
         */
        public static String lowerFirstChar(String str) {
            if (null == str) {
                return null;
            }

            if (str.length() > 0) {
                char firstChar = str.charAt(0);
                if (Character.isUpperCase(firstChar)) {
                    return Character.toLowerCase(firstChar) + str.substring(1);
                }
            }

            return str;
        }

        /**
         * 大写第一位
         *
         * @param str
         * @return
         */
        public static String upperFirstChar(String str) {
            if (null == str) {
                return null;
            }

            if (str.length() > 0) {
                char firstChar = str.charAt(0);
                if (Character.isLowerCase(firstChar)) {
                    return Character.toUpperCase(firstChar) + str.substring(1);
                }
            }

            return str;
        }

        /**
         * 获取第一个分隔符的键和值
         *
         * @param str
         * @param splitStr
         * @return
         */
        public static String[] splitFirst(String str, String splitStr) {
            int i = str.indexOf(splitStr);
            if (i == -1) {
                String[] arr = {str, ""};
                return arr;
            } else {
                String v = "";
                if (str.length() > i + 1) {
                    v = str.substring(i + 1);
                }
                String[] arr = {str.substring(0, i), v};
                return arr;
            }
        }

        /**
         * 修建开始位以 trimStr 开头的字符
         *
         * @param str
         * @param trimStr
         * @return
         */
        public static String trimStart(String str, String trimStr) {
            if (StringUtils.isEmpty(trimStr)) {
                return str;
            }

            if (str.equals(trimStr)) {
                return "";
            }
            int ind = str.indexOf(trimStr);
            if (ind == 0) {
                str = str.substring(trimStr.length());
                return trimStart(str, trimStr);
            }

            return str;
        }

        /**
         * 获取字符串中最后的数字
         *
         * @param str
         * @return
         */
        public static String getEndNumber(String str) {
            List<Character> cls = new ArrayList<>();
            for (int i = str.length() - 1; i > -1; i--) {
                char c = str.charAt(i);
                if (!Character.isDigit(c)) {
                    break;
                }
                cls.add(c);
            }
            if (CollectionUtils.isEmpty(cls)) {
                return "";
            }

            // 反转
            Collections.reverse(cls);

            StringBuilder sb = new StringBuilder();
            for (Character cl : cls) {
                sb.append(cl);
            }

            return sb.toString();
        }

        public static String aa1(String str) {
            return "";
//            char[] carr = new char[str.length()];
//            int j = 0;
//            for (int i = str.length() - 1; i > -1; i--) {
//                char c = str.charAt(i);
//                if (!Character.isDigit(c)) {
//                    break;
//                }
//                carr[carr.length - 1 - j] = c;
//                j++;
//            }
//            if (j == 0) {
//                return "";
//            }
//            // 这里没测试
//            return String.copyValueOf(carr, carr.length - j, j - 1);
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
            return dateTime.format(DateTime_Default_Formatter);
        }

        /**
         * 格式化为：yyyy-MM-dd HH:mm:ss
         *
         * @param dateTime
         * @return
         */
        public static String fmt2Str(Date dateTime) {
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
            return dateTime.format(DateTime_Date_Formatter);
        }

        /**
         * 格式化为：yyyy-MM-dd
         *
         * @param dateTime
         * @return
         */
        public static String fmt2DateStr(Date dateTime) {
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
         * 集合是否为空
         *
         * @param ls
         * @return
         */
        public static boolean isEmpty(Collection<?> ls) {
            return CollectionUtils.isEmpty(ls);
        }

        /**
         * 数组是否为空，支持包装类型
         *
         * @param arr
         * @param <T>
         * @return
         */
        public static <T> boolean isEmptyArray(T[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(boolean[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(char[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(byte[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(int[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(short[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(long[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(float[] arr) {
            return arr == null || arr.length == 0;
        }

        public static boolean isEmptyArray(double[] arr) {
            return arr == null || arr.length == 0;
        }

        /**
         * 集合转数组
         *
         * @param list
         * @param cls  范型集合的元素类型
         * @return
         */
        public static <T> T[] toArray(List<T> list, Class<T> cls) {
            @SuppressWarnings("unchecked") final T[] array = (T[]) Array.newInstance(cls, list.size());
            return list.toArray(array);
        }

        /**
         * 数组转集合
         *
         * @param array
         * @param <T>
         * @return
         */
        public static <T> List<T> toList(T[] array) {
            //Arrays.stream(arr).boxed().collect(Collectors.toList());// 不通用，仅支持int long double 等基础类型
            List<T> resultList = new ArrayList<>(array.length);
            //Collections.addAll(resultList, array);
            for (T item : array) {
                resultList.add(item);
            }

            return resultList;
        }

        /**
         * 数组转集合，使用Stream
         *
         * @param array
         * @param <T>
         * @return
         */
        public static <T> List<T> toListUseStream(T[] array) {
            return Arrays.stream(array)
                    .collect(Collectors.toList());
        }

        /**
         * 将集合转为某字段的Set，会过滤null值
         *
         * @param ls
         * @param getFieldFunc
         * @param filterNull   是否过滤null值
         * @param <T>
         * @param <TField>
         * @return
         */
        public static <T, TField> Set<TField> toFieldSet(List<T> ls, Function<T, TField> getFieldFunc, boolean filterNull) {
            return Optional.ofNullable(ls)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(getFieldFunc)
                    .filter(d -> filterNull ? d != null : true)
                    .collect(Collectors.toSet());
        }

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
         * 分区消费
         *
         * @param ls
         * @param batchSize
         * @param partConsumer
         * @param <T>
         */
        public static <T> void partitionConsumer(List<T> ls, int batchSize, Consumer<List<T>> partConsumer) {
            int size = ls.size();
            for (int i = 0; i < size; i += batchSize) {
                int toIndex = Math.min(i + batchSize, size);
                List<T> data = ls.subList(i, toIndex);
                partConsumer.accept(data);
            }
        }
    }

    // 基础字段处理
    public static class BasicUtil {
        /**
         * 处理字段null，为null时返回默认值，
         *
         * @param t
         * @param defaultVal
         * @param <T>
         * @return
         */
        public static <T> T procFieldNull(T t, T defaultVal) {
            if (t == null) {
                return defaultVal;
            }

            return t;
        }

        /**
         * 处理字符串，为空时返回默认值，其他时去空格
         *
         * @param str
         * @param defaultVal
         * @return
         */
        public static String procFieldEmpty(String str, String defaultVal) {
            if (StringUtils.isBlank(str)) {
                return defaultVal;
            }
            return str.trim();
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
