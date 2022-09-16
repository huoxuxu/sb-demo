package com.hxx.sbcommon.common.basic;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
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
     * 满足条件时，抛出 IllegalArgumentException
     *
     * @param condition 条件
     * @param errMsg
     */
    public static void assertCond(boolean condition, String errMsg) {
        if (condition) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    // Number

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

        // 去除小数点后无意义零
        if (val == val.intValue()) {
            return val.intValue() + "";
        }

        return val + "";
    }

    /**
     * 格式化为字符串，
     * 为null时返回空字符串，
     * 去除1.00后的无意义0
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

        // 去除小数点后无意义零,且不以科学计数发表示
        return val.stripTrailingZeros()
                .toPlainString();
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

    /**
     * 分子乘以100后，与分母相除后保留指定位数
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale       保留精度
     * @return
     */
    public static String roundPercent2Str(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (null == numerator || null == denominator || numerator.compareTo(BigDecimal.ZERO) == 0 || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }

        BigDecimal decimal = numerator.multiply(new BigDecimal(100));
        // 4舍，5入，其他未测
        BigDecimal divVal = roundPercent(decimal, denominator, scale);
        return fmt2Str(divVal);
    }

    // 集合&数组

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
        List<T> resultList = new ArrayList<>(array.length);
        Collections.addAll(resultList, array);

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

    // 日期

    // Date&LocalDateTime
    // yyyy-MM-dd HH:mm:ss
    private final static DateTimeFormatter DateTime_Default_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd " + "HH:mm:ss");
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
    public static String fmt2DateStr(LocalDateTime dateTime) {
        return dateTime.format(DateTime_Date_Formatter);
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
        try {
            return LocalDateTime.parse(text, DateTime_Default_Formatter);
        } catch (Exception e) {
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

    // 字符串

    /**
     * 分割字符串
     *
     * @param oriStr
     * @param separatorChars
     * @return
     */
    public static List<String> Split(String oriStr, String separatorChars) {
        List<String> ls = new ArrayList<>();
        if (StringUtils.isBlank(oriStr)) {
            return ls;
        }

        String[] arr = StringUtils.split(oriStr, separatorChars);
        for (String item : arr) {
            if (StringUtils.isBlank(item)) {
                continue;
            }

            ls.add(item.trim());
        }

        return ls;
    }

    /**
     * 替换字符串
     *
     * @param oriStr
     * @param str1
     * @param newStr1
     * @return
     */
    public static String Replace(String oriStr, String str1, String newStr1) {
        return StringUtils.replace(oriStr, str1, newStr1);
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

    // 数据库

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
