package com.hxx.sbcommon.common.basic.langType.impl;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.json.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-10 8:45:50
 **/
public class ListTypeHandler implements LangTypeHandler<List<?>> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public List<?> convert(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof List) {
            return (List<?>) val;
        }

        // 判断是否数组
        if (val instanceof Arrays) {
            return null;
        }

        if (val instanceof String) {
            String vv = (String) val;
            if (!StringUtils.isEmpty(vv) && vv.trim().startsWith("[")) {
                return JsonUtil.parseArray(vv, List.class);
            }
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }

}
