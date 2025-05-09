package com.hxx.sbcommon.common;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WASD {
    // yyyy-MM-dd HH:mm:ss
    private final static DateTimeFormatter DateTime_Default_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // yyyy-MM-dd
    private final static DateTimeFormatter DateTime_Date_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void ast(boolean condition, String msg) {
        if (condition) {
            throw new RuntimeException(msg);
        }
    }

    public static void ast(boolean condition, RuntimeException ex) {
        if (condition) {
            throw ex;
        }
    }

    /**
     * 值为null则返回默认值
     *
     * @param t
     * @param defaultVal
     * @param <T>
     * @return
     */
    public static <T> T nullToDefault(T t, T defaultVal) {
        return t == null ? defaultVal : t;
    }

    /**
     * 值为null则返回默认值
     *
     * @param t
     * @param getFieldFunc
     * @param defaultVal
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> TField nullToDefault(T t, Function<T, TField> getFieldFunc, TField defaultVal) {
        return Optional.ofNullable(t)
                .map(getFieldFunc)
                .orElse(defaultVal);
    }

    /**
     * 解析为LocalDateTime，支持字符串格式：yyyy-MM-dd HH:mm:ss
     *
     * @param text
     * @return
     */
    public static LocalDateTime parseDateTime(String text) {
        return LocalDateTime.parse(text, DateTime_Default_Formatter);
    }

    /**
     * Date转LocalDateTime
     *
     * @param date Date
     * @return
     */
    public static LocalDateTime parse(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 格式化为：yyyy-MM-dd
     *
     * @param dateTime
     * @return
     */
    public static String fmt2Date(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTime_Date_Formatter);
    }

    /**
     * 转Date
     *
     * @param dt
     * @return
     */
    public static Date toDate(LocalDateTime dt) {
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 格式化为：yyyy-MM-dd HH:mm:ss
     *
     * @param dateTime
     * @return
     */
    public static String fmt2Str(Date dateTime) {
        if (dateTime == null) {
            return "";
        }

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(dateTime);
    }

    /**
     * 两数相除保留指定精度
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale       精度
     * @return
     */
    public static BigDecimal divide(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (null == numerator || null == denominator
                || numerator.compareTo(BigDecimal.ZERO) == 0
                || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        // 4舍，5入，其他未测
        return numerator.divide(denominator, scale, RoundingMode.HALF_UP);
    }

    /**
     * 转换为不以科学计数法的字符串，且去除小数点后无意义的零
     *
     * @param b
     * @return
     */
    public static String format(BigDecimal b) {
        // stripTrailingZeros 去除小数点后无意义的零
        // toPlainString 返回不以指数表示的字符串形式
        return b.stripTrailingZeros()
                .toPlainString();
    }

    /**
     * 字符串分割，支持多分隔符
     *
     * @param str
     * @param splitStrs
     * @return
     */
    public static Set<String> splitAsSet(String str, String... splitStrs) {
        List<String> ls = splitByWholeSeparators(str, splitStrs);
        return new HashSet<>(ls);
    }

    /**
     * 字符串分割，支持多分隔符
     * 原始字符串："1,2,3，4"
     * 分隔符：",","，"
     * 结果：1 2 3 4
     *
     * @param str
     * @param splitStrs
     * @return
     */
    public static List<String> splitByWholeSeparators(String str, String... splitStrs) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return new ArrayList<>();
        }
        if (splitStrs == null || splitStrs.length == 0) {
            return new ArrayList<>(Arrays.asList(str));
        }

        List<String> ls = new ArrayList<>();
        for (String splitStr : splitStrs) {
            if (ls.size() == 0) {
                String[] arr = org.apache.commons.lang3.StringUtils.splitByWholeSeparator(str, splitStr);
                for (String item : arr) {
                    if (!org.apache.commons.lang3.StringUtils.isBlank(item)) {
                        ls.add(item.trim());
                    }
                }
            } else {
                List<String> result = splits(ls, splitStr);
                ls = result;
            }
        }
        return ls;
    }

    /**
     * 输入字符串集合，集合元素都按 分隔符 分割后去除空或空格后返回,
     *
     * @param strs
     * @param splitStr
     * @return
     */
    public static List<String> splits(List<String> strs, String splitStr) {
        List<String> ls = new ArrayList<>();
        for (String str : strs) {
            if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
                continue;
            }

            String[] arr = org.apache.commons.lang3.StringUtils.splitByWholeSeparator(str, splitStr);
            for (String item : arr) {
                if (!StringUtils.isBlank(item)) {
                    ls.add(item.trim());
                }
            }
        }
        return ls;
    }

    /**
     * 过滤空且去空格
     *
     * @param ls
     * @return
     */
    public static List<String> filterEmpty(Collection<String> ls) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> !StringUtils.isBlank(d))
                .map(d -> d.trim())
                .collect(Collectors.toList());
    }

    /**
     * 过滤空且去空格
     *
     * @param ls
     * @return
     */
    public static Set<String> filterEmptyToSet(Collection<String> ls) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> !StringUtils.isBlank(d))
                .map(d -> d.trim())
                .collect(Collectors.toSet());
    }

    /**
     * 返回符合条件的个数
     *
     * @param ls
     * @param filterFunc
     * @param <T>
     * @return
     */
    public static <T> long count(Collection<T> ls, Predicate<T> filterFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(filterFunc)
                .count();
    }

    /**
     * 重新映射
     *
     * @param ls
     * @param fieldGetter
     * @param filterFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> mapField(Collection<T> ls, Function<T, R> fieldGetter, Predicate<R> filterFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .filter(filterFunc)
                .collect(Collectors.toList());
    }

    /**
     * 重新投影
     *
     * @param ls
     * @param fieldGetter
     * @param filterFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Set<R> mapFieldToSet(Collection<T> ls, Function<T, R> fieldGetter, Predicate<R> filterFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .filter(filterFunc)
                .collect(Collectors.toSet());
    }

    /**
     * 重新投影
     *
     * @param ls
     * @param fieldGetter
     * @param filterFunc
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> SortedSet<R> mapFieldToSortedSet(Collection<T> ls, Function<T, R> fieldGetter, Predicate<R> filterFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .filter(filterFunc)
                .collect(Collectors.toCollection(TreeSet::new));
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
    public static <T> T firstOrDefault(Collection<T> ls, Predicate<T> predicate, T defaultVal) {
        if (ls == null || ls.isEmpty()) {
            return defaultVal;
        }

        return ls.stream()
                .filter(predicate)
                .findFirst()
                .orElse(defaultVal);
    }

    /**
     * 是否有任一符合条件的项
     *
     * @param ls
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean any(Collection<T> ls, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(ls)) {
            return false;
        }

        return ls.stream().anyMatch(predicate);
    }

    /**
     * 是否全部项都符合条件
     *
     * @param ls
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean all(Collection<T> ls, Predicate<T> predicate) {
        if (CollectionUtils.isEmpty(ls)) {
            return false;
        }

        return ls.stream().allMatch(predicate);
    }

    /**
     * 简单的分组
     *
     * @param ls
     * @param filter
     * @param groupByFunc
     * @param <T>
     * @param <TK>
     * @return
     */
    public static <T, TK> Map<TK, List<T>> groupByMap(Collection<T> ls,
                                                      Predicate<? super T> filter,
                                                      Function<? super T, ? extends TK> groupByFunc) {
        return groupByMap(ls, filter, groupByFunc, d -> d);
    }

    /**
     * 简单的分组
     *
     * @param ls
     * @param filter
     * @param groupByFunc
     * @param groupByValFunc
     * @param <T>
     * @param <TK>
     * @param <TV>
     * @return
     */
    public static <T, TK, TV> Map<TK, TV> groupByMap(Collection<T> ls,
                                                     Predicate<? super T> filter,
                                                     Function<? super T, ? extends TK> groupByFunc,
                                                     Function<? super List<T>, ? extends TV> groupByValFunc) {
        return groupByMap(ls, filter, groupByFunc, (k, v) -> groupByValFunc.apply(v));
    }

    /**
     * 简单的分组
     *
     * @param ls
     * @param filter
     * @param groupByFunc
     * @param groupByValFunc
     * @param <T>
     * @param <TK>
     * @param <TV>
     * @return
     */
    public static <T, TK, TV> Map<TK, TV> groupByMap(Collection<T> ls,
                                                     Predicate<? super T> filter,
                                                     Function<? super T, ? extends TK> groupByFunc,
                                                     BiFunction<TK, ? super List<T>, ? extends TV> groupByValFunc) {
        if (ls == null || ls.isEmpty()) {
            return new HashMap<>();
        }

        Map<TK, TV> map = new HashMap<>();
        ls.stream()
                .filter(filter)
                .collect(Collectors.groupingBy(groupByFunc))
                .forEach((k, v) -> map.put(k, groupByValFunc.apply(k, v)));
        return map;
    }

    /**
     * 返回按指定字段去重后的集合
     *
     * @param ls
     * @param distinctByGetter
     * @param <T>
     * @return
     */
    public static <T> List<T> distinctBy(Collection<T> ls, Function<T, String> distinctByGetter) {
        if (ls == null || ls.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> set = new HashSet<>();
        List<T> results = new ArrayList<>();
        for (T t : ls) {
            String field = distinctByGetter.apply(t);
            if (set.add(field)) {
                results.add(t);
            }
        }
        return results;
    }

    /**
     * 排序
     *
     * @param ls
     * @param getFieldFunc
     * @param asc
     * @param limit        -1全部
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField extends Comparable<TField>> List<T> sortList(Collection<T> ls,
                                                                          Function<T, TField> getFieldFunc,
                                                                          boolean asc,
                                                                          int limit) {
        if (ls == null || ls.isEmpty()) {
            return new ArrayList<>();
        }

        Comparator<T> comparing;
        if (asc) {
            comparing = Comparator.comparing(getFieldFunc);
        } else {
            comparing = Comparator.comparing(getFieldFunc, Comparator.reverseOrder());
        }
        List<T> nullData = new ArrayList<>();
        List<T> notNullData = new ArrayList<>();

        for (T d : ls) {
            if (getFieldFunc.apply(d) == null) {
                nullData.add(d);
            } else {
                notNullData.add(d);
            }
        }

        notNullData.sort(comparing);
        notNullData.addAll(nullData);
        if (limit < 0) {
            return notNullData;
        }

        return notNullData.stream().limit(limit).collect(Collectors.toList());
    }

}
