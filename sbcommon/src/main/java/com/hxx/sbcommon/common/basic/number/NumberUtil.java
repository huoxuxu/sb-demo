package com.hxx.sbcommon.common.basic.number;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-07-06 9:24:08
 **/
public class NumberUtil {
    /**
     * 格式化为字符串，
     * 为null时返回空字符串，
     * 去除1.00后的无意义0
     *
     * @param val
     * @return
     */
    public static String fmt2String(Float val) {
        if (val == null) {
            return "";
        }

        if (val == 0) {
            return "0";
        }

        // 去除小数点后无意义零
        if (val == val.intValue()) {
            return String.valueOf(val.intValue());
        }

        return String.valueOf(val);
    }

    /**
     * 两数相除后保留n位小数，带百分号后缀
     *
     * @param numerator   分子
     * @param denominator 分母
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
     * 两数相除后保留n位小数，带百分号后缀
     *
     * @param numerator   分子
     * @param denominator 分母
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
