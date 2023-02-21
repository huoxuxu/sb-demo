package com.hxx.sbweb.common;

/**
 * 计算耗时
 */
public class CostTimeUtils {
    /**
     * @return 当前毫秒数
     */
    public static long nowMs() {
        return System.currentTimeMillis();
    }

    /**
     * 当前毫秒与起始毫秒差
     * @param startMillis 开始纳秒数
     * @return 时间差
     */
    public static long diffMs(long startMillis) {
        return nowMs()-startMillis;
    }


}
