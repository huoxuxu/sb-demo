package com.hxx.sbcommon.common.reflect;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Description: Bean 转换工具类.
 * @author:
 * @Date: 2020/6/18 14:15
 */
@Slf4j
public class CopyObjUtil {
    /**
     * 将数据源的属性拷贝到目标对象
     *
     * @param src  源对象
     * @param dest 目标对象
     * @param <T>
     * @throws Exception
     */
    public static <T> void copyTo(T src, T dest) {
        ReflectorObj reflectorObj = new ReflectorObj(dest.getClass());
        Map<String, Object> objValMap = reflectorObj.getObjMap(src);
        reflectorObj.setInstance(dest, objValMap);
    }

    /**
     * 将数据源的属性拷贝到目标对象
     * 只拷贝目标对象包含的属性
     *
     * @param src  源对象
     * @param dest 目标对象
     * @param <T>
     * @throws Exception
     */
    public static <T> void copyToObj(T src, Object dest) {
        ReflectorObj reflectorObj = new ReflectorObj(dest.getClass());
        Map<String, Object> objValMap = reflectorObj.getObjMap(src);
        reflectorObj.setInstance(dest, objValMap);
    }


}
