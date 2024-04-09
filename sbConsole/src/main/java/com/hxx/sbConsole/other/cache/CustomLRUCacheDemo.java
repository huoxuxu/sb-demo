package com.hxx.sbConsole.other.cache;

import com.hxx.sbcommon.common.cache.CustomLRUCache;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-02-17 16:24:26
 **/
public class CustomLRUCacheDemo {

    public static void main(String[] args) {
        try {
            CustomLRUCache<Integer> cache = new CustomLRUCache<>();

            Integer val1 = cache.get(0);
            cache.put(1, 1);
            cache.put(2, 2);
            cache.put(3, 3);
            Integer val2 = cache.get(2);
            cache.put(2, 22);
            Integer val3 = cache.get(2);

            System.out.println("ok");
        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }


}
