package com.hxx.sbcommon.common.basic;

import java.math.BigDecimal;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-07-01 9:25:22
 **/
public class BigDecimalUtil {
    /**
     * 保留scale 位小数
     *
     * @param b
     * @param scale
     * @return
     */
    public static BigDecimal ROUND_HALF_UP(BigDecimal b, int scale) {
//        float operateLengthNum=0F;
//        BigDecimal b = new BigDecimal(operateLengthNum);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP);
        //.floatValue();
    }

    /**
     * 入参乘以100后保留指定位数的百分数。
     * eg.99.12%
     *
     * @param b     0.9912
     * @param scale 2
     * @return
     */
    public static BigDecimal setPercentScaleNum(BigDecimal b, int scale) {
        if (b == null || b.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal bd = b.multiply(BigDecimal.valueOf(100));
        return bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 分子分母相除后保留指定位数
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale
     * @return
     */
    public static BigDecimal round(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (null == numerator || null == denominator
                || numerator.compareTo(BigDecimal.ZERO) == 0
                || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(0);
        }
        BigDecimal decimal = numerator.multiply(new BigDecimal(100));
        // 4舍，5入，其他未测
        return decimal.divide(denominator, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 转换为不以科学计数法的字符串，且去除小数点后无意义的零
     *
     * @param b
     * @return
     */
    public static String Format(BigDecimal b) {
        // stripTrailingZeros 去除小数点后无意义的零
        // toPlainString 返回不以指数表示的字符串形式
        return b.stripTrailingZeros().toPlainString();
    }

}
