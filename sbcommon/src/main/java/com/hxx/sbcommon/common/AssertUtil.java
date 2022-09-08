package com.hxx.sbcommon.common;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 16:56:05
 **/
public class AssertUtil {

    /**
     * 不满足条件时，抛出 IllegalArgumentException
     *
     * @param condition
     * @param errMsg
     */

    public static void Assert(Boolean condition, String errMsg) {
        if (condition == null || !condition) {
            throw new IllegalArgumentException(errMsg);
        }
    }
}
