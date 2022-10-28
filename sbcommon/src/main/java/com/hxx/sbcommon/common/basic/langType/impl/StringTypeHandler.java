package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 10:22:30
 **/
public class StringTypeHandler implements LangTypeHandler<String> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public String convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof String) {
            return (String) val;
        }

        return val + "";
//        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
