package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-26 18:07:54
 **/
public class BooleanTypeHandler implements LangTypeHandler<Boolean> {

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Boolean convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Boolean) {
            return (Boolean) val;
        }

        if (val instanceof String) {
            return Boolean.parseBoolean((String)val);
        }


        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
