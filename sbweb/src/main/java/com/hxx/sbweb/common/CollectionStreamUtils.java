package com.hxx.sbweb.common;

import com.hxx.sbweb.model.StuModel;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-13 17:39:35
 **/
public class CollectionStreamUtils {
    // 转换 map
    public static void map(List<Integer> ids) {
        List<String> ls = ids.stream().map(d -> d + "").collect(Collectors.toList());

    }

    // 排序
    public static void sorted(List<Integer> ids) {
        Stream<Integer> ls = ids.stream().sorted();

    }

    public static void sortedForModel(List<StuModel> stus) {
        // 按模型中指定字段排序
        Comparator<StuModel> stuModelComparator = Comparator
                .comparing(StuModel::getScore)
                .thenComparing(StuModel::getBirthday);
        stus.stream().sorted(stuModelComparator).collect(Collectors.toList());

    }

    // 过滤
    public static void filter(List<Integer> ids) {
        ids.stream().filter(d -> d > 9).collect(Collectors.toList());

    }

    public static void filterForModel(List<StuModel> stus) {
        stus.stream().filter(d -> d.getScore() > 60).collect(Collectors.toList());

    }

    // 分组
    public static void groupby(List<StuModel> stus) {
        // 按 Code和Score 分組
        stus.stream().collect(Collectors.groupingBy(StuModel::getCode, Collectors.groupingBy(StuModel::getScore)));

    }

    // 聚合
    public static void min(List<Integer> ids) {
        ids.stream().min(Comparator.comparing(Integer::intValue));

    }

    public static void max(List<Integer> ids) {
        ids.stream().max(Comparator.comparing(Integer::intValue));

    }

    public static void count(List<Integer> ids) {
        ids.stream().count();

    }

    // 分区
    public static void skip(List<Integer> ids) {
        ids.stream().skip(1).collect(Collectors.toList());

    }

    public static void limit(List<Integer> ids) {
        ids.stream().limit(2).collect(Collectors.toList());

    }

    // 查询
    // anyMatch 有一个元素满足返回true
    public static void anyMatch(List<Integer> ids) {
        boolean b = ids.stream().anyMatch(d -> d > 0);

    }
    
    // allMatch 全部元素满足返回true
    public static void allMatch(List<Integer> ids) {
        boolean b = ids.stream().allMatch(d -> d > 0);

    }

    // noneMatch 没有元素满足返回true
    public static void noneMatch(List<Integer> ids) {
        boolean b = ids.stream().noneMatch(d -> d > 0);

    }
    // findFirst 返回第一个元素的Optional，无元素返回空的Optional
    public static void findFirst(List<Integer> ids) {
        Optional<Integer> first = ids.stream().findFirst();
        // 判断查询的类对象是否存在
        boolean present = first.isPresent();
        if(present){
            Integer cur = first.get();
        }

    }


}
