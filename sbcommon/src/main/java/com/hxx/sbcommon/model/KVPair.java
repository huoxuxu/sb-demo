package com.hxx.sbcommon.model;

/**
 * 键值对模型
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-11 16:49:06
 **/
@lombok.Data
public class KVPair<T> {
    /**
     * 键
     */
    private String key;
    /**
     * 值
     */
    private T val;

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
    public KVPair(String key, T val) {
        this.key = key;
        this.val = val;
    }

}
