package com.hxx.sbcommon.common;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-06 9:24:08
 **/
public class NumberUtil {
    /**
     * 保留n位小数
     *
     * @param numerator
     * @param denominator
     * @param scale
     * @return
     */
    public static String round(Integer numerator, Integer denominator, int scale) {
        if (0 == denominator || null == denominator || null == numerator) {
            return 0 + "%";
        }

        BigDecimal b1 = new BigDecimal(numerator);
        BigDecimal b2 = new BigDecimal(denominator);

        // 4舍，5入，其他未测
        BigDecimal v1 = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);

        return v1 + "%";
    }

    /**
     * @param numerator
     * @param denominator
     * @param scale
     * @return
     */
    public static String round(Double numerator, Double denominator, int scale) {
        if (0 == denominator || null == denominator || null == numerator) {
            return 0 + "%";
        }

        BigDecimal b1 = new BigDecimal(numerator);
        BigDecimal b2 = new BigDecimal(denominator);

        // 4舍，5入，其他未测
        BigDecimal v1 = b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);

        return v1 + "%";
    }


}
