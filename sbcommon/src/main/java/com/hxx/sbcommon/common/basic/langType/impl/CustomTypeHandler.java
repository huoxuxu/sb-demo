package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.json.JsonUtil;

/**
 * 自定义
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-27 11:10:27
 **/
public class CustomTypeHandler implements LangTypeHandler<Object> {
    private Class<?> targetClass;

    public CustomTypeHandler(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Object convert(Object val) {
        if (val == null) {
            return null;
        }

        // 不能先判断，貌似所有类型都是true。。。
//        if (val instanceof Object) {
//            return val;
//        }

        if (val instanceof String) {
            String str = (String) val;
            str = str.trim();
            if (str.startsWith("{")) {
                return JsonUtil.parse(str, targetClass);
            } else if (str.startsWith("[")) {
                // 获取泛型类型
                return JsonUtil.parseArray(str, targetClass);
            }
        }

        throw new IllegalArgumentException("目前仅支持json格式的自定义类型");

    }
}
