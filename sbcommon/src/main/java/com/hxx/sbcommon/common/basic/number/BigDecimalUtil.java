package com.hxx.sbcommon.common.basic.number;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-07-01 9:25:22
 **/
public class BigDecimalUtil {
    /**
     * 直接去掉多余的位数
     *
     * @return
     */
    public static BigDecimal ROUND_DOWN(BigDecimal b, int scale) {
        BigDecimal val = b.setScale(scale, BigDecimal.ROUND_DOWN);
        return val;
    }

    /**
     * 保留scale 位小数, 四舍五入
     *
     * @param b
     * @param scale
     * @return
     */
    public static BigDecimal ROUND_HALF_UP(BigDecimal b, int scale) {
        BigDecimal val = b.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return val;
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
     * 分子乘以100后，与分母相除后保留指定位数
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale
     * @return
     */
    public static BigDecimal round(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (null == numerator || null == denominator || numerator.compareTo(BigDecimal.ZERO) == 0 || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(0);
        }

        BigDecimal decimal = numerator.multiply(new BigDecimal(100));
        // 4舍，5入，其他未测
        return decimal.divide(denominator, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两数相除保留指定精度
     *
     * @param numerator   分子
     * @param denominator 分母
     * @param scale       精度
     * @return
     */
    public static BigDecimal divide(BigDecimal numerator, BigDecimal denominator, int scale) {
        if (null == numerator || null == denominator || numerator.compareTo(BigDecimal.ZERO) == 0 || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(0);
        }

        // 4舍，5入，其他未测
        return numerator.divide(denominator, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 两个数相乘，保留指定位数
     *
     * @param val
     * @param bval
     * @param scale
     * @return
     */
    public static BigDecimal multiply(BigDecimal val, BigDecimal bval, int scale) {
        if (null == val || null == bval) {
            return null;
        }
        if (val.compareTo(BigDecimal.ZERO) == 0 || bval.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(0);
        }
        return val.multiply(bval).setScale(scale, RoundingMode.HALF_UP);
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
        return b.stripTrailingZeros()
                .toPlainString();
    }

}
