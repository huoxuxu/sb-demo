package com.hxx.sbConsole.service.common.typeHandler;

import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbConsole.model.KV;
import com.hxx.sbcommon.common.basic.langType.LangTypeHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-23 14:18:50
 **/
public class KVTypeHandler implements LangTypeHandler<KV> {
    /**
     * 将任意类型转为当前类型
     *
     * @param val 待转型的值
     * @return
     */
    @Override
    public KV change(Object val) {
        if (val == null) return null;

        if (val instanceof DemoCls) {
            DemoCls demoCls = (DemoCls) val;
            return new KV(demoCls.getId(), demoCls.getName());
        }

        throw new IllegalArgumentException("转换类型失败，提供值：" + val);
    }
}
