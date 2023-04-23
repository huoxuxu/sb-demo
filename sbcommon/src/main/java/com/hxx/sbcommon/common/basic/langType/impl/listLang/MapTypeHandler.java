package com.hxx.sbcommon.common.basic.langType.impl.listLang;

import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;
import com.hxx.sbcommon.common.json.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-09 16:49:18
 **/
public class MapTypeHandler implements LangTypeHandler<Map<?, ?>> {
    /**
     * @param val 待转型的值
     * @return
     */
    @Override
    public Map<?, ?> change(Object val) {
        if (val == null) {
            return null;
        }
        if (val instanceof Map) {
            return (Map<?, ?>) val;
        }

        if (val instanceof String) {
            String vv = (String) val;
            if (!StringUtils.isEmpty(vv) && vv.trim().startsWith("{")) {
                return JsonUtil.parse(vv, Map.class);
            }
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
