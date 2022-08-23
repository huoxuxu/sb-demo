package com.hxx.sbcommon.common;

import lombok.extern.slf4j.Slf4j;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

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
     * @param cls 范型集合的元素类型
     * @return
     */
    public static <T> T[] toArray(List<T> list, Class<T> cls) {
        // 使用原生的反射方法，在运行时知道其数组对象类型
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

}
