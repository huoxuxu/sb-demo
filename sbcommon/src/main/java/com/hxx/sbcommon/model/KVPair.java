package com.hxx.sbcommon.model;

/**
 * 键值对模型
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 16:49:06
 **/
@lombok.Data
public class KVPair<TKey, TVal> {
    /**
     * 键
     */
    private TKey key;
    /**
     * 值
     */
    private TVal val;

    /**
     * 空构造
     */
    public KVPair() {
    }

    /**
     * 全构造
     *
     * @param key
     * @param val
     */
    public KVPair(TKey key, TVal val) {
        this.key = key;
        this.val = val;
    }

}
