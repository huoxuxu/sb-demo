package com.hxx.sbcommon.common.basic.langType;

/**
 * 将任意类型转为T
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-26 15:49:37
 **/
public interface LangTypeHandler<T> {
    /**
     * 将任意类型转为当前类型
     *
     * @param val 待转型的值
     * @return
     */
    T change(Object val);
}