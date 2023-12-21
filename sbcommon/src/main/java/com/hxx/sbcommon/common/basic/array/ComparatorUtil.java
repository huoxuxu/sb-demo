package com.hxx.sbcommon.common.basic.array;

import com.hxx.sbcommon.model.KVPair;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * 比较器
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-20 16:37:43
 **/
public class ComparatorUtil {

    /**
     * 获取排序规则
     *
     * @param comls
     * @param <T>
     * @return
     */
    public static <T> Comparator<T> get(List<KVPair<Function<T, Comparable>, Boolean>> comls) {
        Comparator<T> comparing = null;
        for (int j = 0; j < comls.size(); j++) {
            KVPair<Function<T, Comparable>, Boolean> item = comls.get(j);
            if (j == 0) {
                if (item.getVal()) {
                    comparing = Comparator.comparing(item.getKey());
                } else {
                    comparing = Comparator.comparing(item.getKey(), Comparator.reverseOrder());
                }
            } else {
                if (item.getVal()) {
                    comparing = comparing.thenComparing(item.getKey());
                } else {
                    comparing = comparing.thenComparing(item.getKey(), Comparator.reverseOrder());
                }
            }
        }
        return comparing;
    }


}
