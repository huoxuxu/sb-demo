package com.hxx.sbcommon.common.basic.langType;

/**
 * 目标类型
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-26 15:49:37
 **/
public interface LangTypeHandler<T> {
    /**
     * @param val 待转型的值
     * @return
     */
    T convert(Object val);
}