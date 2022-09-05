package com.hxx.sbcommon.common.basic.array;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-22 8:56:28
 **/
@Slf4j
public class ListUtil {
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
        return Arrays.stream(array).collect(Collectors.toList());
    }

}
