package com.hxx.sbcommon.common.basic.array;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-22 8:56:28
 **/
@Slf4j
public class CollectionUtil {
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
        // 可简单使用: Arrays.asList(array)
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

    /**
     * 移除集合中，满足指定条件的项
     *
     * @param list
     * @param delPred
     * @param <T>
     */
    public static <T> void remove(List<T> list, Predicate<T> delPred) {
        for (int i = list.size() - 1; i > -1; i--) {
            if (delPred.test(list.get(i))) {
                list.remove(list.get(i));
            }
        }
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

    /**
     * 字符串集合 去空、去空格
     *
     * @param ls
     * @return
     */
    public static List<String> getItemList(Collection<String> ls) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> !StringUtils.isBlank(d))
                .map(d -> d.trim())
                .collect(Collectors.toList());
    }

    /**
     * 字符串集合去空，去前后空格，去重
     *
     * @param ls
     * @return
     */
    public static Set<String> getItemSet(Collection<String> ls) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> !StringUtils.isBlank(d))
                .map(d -> d.trim())
                .collect(Collectors.toSet());
    }

    /**
     * 排序集合，按字段
     * 支持多字段连接为字符串排序
     *
     * @param ls
     * @param getOrderFieldFunc 排序字段
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField extends Comparable<TField>> List<T> sortList(Collection<T> ls, Function<T, TField> getOrderFieldFunc, boolean asc) {
        if (CollectionUtils.isEmpty(ls)) return new ArrayList<>();

        List<T> nullData = ls.stream()
                .filter(d -> getOrderFieldFunc.apply(d) == null)
                .collect(Collectors.toList());
        Comparator<T> comparing;
        if (asc) comparing = Comparator.comparing(getOrderFieldFunc);
        else comparing = Comparator.comparing(getOrderFieldFunc, Comparator.reverseOrder());
        List<T> notNullData = ls.stream()
                .filter(d -> getOrderFieldFunc.apply(d) != null)
                .sorted(comparing)
                .collect(Collectors.toList());
        notNullData.addAll(nullData);
        return notNullData;
    }

    /**
     * 按中文拼音排序
     *
     * @param ls
     * @param getOrderFieldFunc
     * @param asc
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField extends Comparable<TField>> List<T> sortListUseCollator(Collection<T> ls, Function<T, TField> getOrderFieldFunc, boolean asc) {
        if (CollectionUtils.isEmpty(ls)) return new ArrayList<>();

        List<T> nullData = ls.stream()
                .filter(d -> getOrderFieldFunc.apply(d) == null)
                .collect(Collectors.toList());
        Comparator<T> collatorComparator = (T o1, T o2) -> {
            Collator instance = Collator.getInstance(Locale.CHINA);
            if (asc)
                return instance.compare(getOrderFieldFunc.apply(o1), getOrderFieldFunc.apply(o2));
            else
                return instance.compare(getOrderFieldFunc.apply(o2), getOrderFieldFunc.apply(o1));
        };
        List<T> notNullData = ls.stream()
                .filter(d -> getOrderFieldFunc.apply(d) != null)
                .sorted(collatorComparator)
                .collect(Collectors.toList());
        notNullData.addAll(nullData);
        return notNullData;
    }

    /**
     * 集合投影指定字段
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> List<TField> getFieldList(Collection<T> ls, Function<T, TField> fieldGetter, Predicate<TField> filterPred) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .filter(filterPred)
                .collect(Collectors.toList());
    }

    /**
     * 集合投影指定字段
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> List<TField> getFieldList(Collection<T> ls, Function<T, TField> fieldGetter) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .collect(Collectors.toList());
    }

    /**
     * 获取字段值Set集合
     *
     * @param ls
     * @param getFieldFunc
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> Set<TField> getFieldSet(Collection<T> ls, Predicate<T> filterPred, Function<T, TField> getFieldFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(filterPred)
                .map(getFieldFunc)
                .collect(Collectors.toSet());
    }

    /**
     * 获取字段值Set集合
     *
     * @param ls
     * @param getFieldFunc
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> Set<TField> getFieldSet(Collection<T> ls, Function<T, TField> getFieldFunc) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(getFieldFunc)
                .collect(Collectors.toSet());
    }

    /**
     * 获取字段值SortedSet集合
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField> SortedSet<TField> getFieldSortedSet(Collection<T> ls, Function<T, TField> fieldGetter, Predicate<TField> filterPred) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .map(fieldGetter)
                .filter(filterPred)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * 获取最小的项
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField extends Comparable<TField>> T getMin(Collection<T> ls, Function<T, TField> fieldGetter) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> fieldGetter.apply(d) != null)
                .min(Comparator.comparing(fieldGetter))
                .orElse(null);
    }

    public static <T, TField extends Comparable<TField>> T getMax(Collection<T> ls, Function<T, TField> fieldGetter) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> fieldGetter.apply(d) != null)
                .max(Comparator.comparing(fieldGetter))
                .orElse(null);
    }

    /**
     * 获取最小的项
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @param <TField>
     * @return
     */
    public static <T, TField extends Comparable<TField>> TField getMinField(Collection<T> ls, Function<T, TField> fieldGetter, TField defaultVal) {
        T t = Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> fieldGetter.apply(d) != null)
                .min(Comparator.comparing(fieldGetter))
                .orElse(null);
        return t == null ? defaultVal : fieldGetter.apply(t);
    }

    public static <T, TField extends Comparable<TField>> TField getMaxField(Collection<T> ls, Function<T, TField> fieldGetter, TField defaultVal) {
        T t = Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> fieldGetter.apply(d) != null)
                .max(Comparator.comparing(fieldGetter))
                .orElse(null);
        return t == null ? defaultVal : fieldGetter.apply(t);
    }

    /**
     * 求和
     *
     * @param ls
     * @param fieldGetter
     * @param <T>
     * @return
     */
    public static <T> BigDecimal getSumBigDecimal(Collection<T> ls, Function<T, BigDecimal> fieldGetter) {
        return Optional.ofNullable(ls).orElse(new ArrayList<>())
                .stream()
                .filter(d -> fieldGetter.apply(d) != null)
                .map(fieldGetter)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * 求平均
     *
     * @param ls
     * @param fieldGetter
     * @param scale
     * @param <T>
     * @return
     */
    public static <T> BigDecimal getAvgBigDecimal(Collection<T> ls, Function<T, BigDecimal> fieldGetter, int scale) {
        int cou = ls == null ? 0 : ls.size();
        BigDecimal sum = getSumBigDecimal(ls, fieldGetter);
        if (sum == null || cou == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(cou), scale, RoundingMode.HALF_UP);
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
    public static <T, TK> Map<TK, List<T>> getGroupByMap(Collection<T> ls, Predicate<? super T> filter, Function<? super T, ? extends TK> groupByFunc) {
        if (filter == null) filter = (T t) -> true;
        return Optional.ofNullable(ls)
                .orElse(new ArrayList<>())
                .stream()
                .filter(filter)
                .collect(Collectors.groupingBy(groupByFunc));
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
    public static <T, TK, TV> Map<TK, TV> getGroupByMap(Collection<T> ls, Predicate<? super T> filter,
                                                        Function<? super T, ? extends TK> groupByFunc,
                                                        BiFunction<TK, ? super List<T>, ? extends TV> groupByValFunc) {
        Map<TK, List<T>> groupByMap = getGroupByMap(ls, filter, groupByFunc);
        Map<TK, TV> map = new HashMap<>();
        for (Map.Entry<TK, List<T>> entry : groupByMap.entrySet()) {
            TK key = entry.getKey();
            map.put(key, groupByValFunc.apply(key, entry.getValue()));
        }
        return map;
    }

    /**
     * 交集
     * Apache的CollectionUtil 提供集合相减能力
     *
     * @param src
     * @param tar
     * @param <T>
     * @return
     */
    public static <T> Set<T> intersect(Collection<T> src, Set<T> tar) {
        if (CollectionUtils.isEmpty(src) || CollectionUtils.isEmpty(tar)) {
            return new HashSet<>();
        }
        return src.stream()
                .filter(d -> tar.contains(d))
                .collect(Collectors.toSet());
    }

    /**
     * 集合相减
     *
     * @param
     * @param tar
     * @param <T>
     * @return
     */
    public static <T> Collection<T> subtract(Collection<T> src, Collection<T> tar) {
        return org.apache.commons.collections4.CollectionUtils.subtract(src, tar);
    }

    /**
     * 获取map中keys对应的val值集合
     *
     * @param map
     * @param keys
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> List<V> getMapValByKeys(Map<T, V> map, Collection<T> keys) {
        if (map == null || map.size() == 0 || CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        return keys.stream()
                .filter(map::containsKey)
                .map(map::get)
                .collect(Collectors.toList());
    }

}
