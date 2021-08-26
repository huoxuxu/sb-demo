package com.hxx.sbcommon.common;

import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description: 类型转换
 * @Date: 2021-05-10 14:21:26
 **/
public class TypeChangeUtil {
    /**
     * 转整型
     *
     * @param str
     * @param defaultVal
     * @return
     */
    public static Integer parseInteger(String str, Integer defaultVal) {
        return NumberUtils.toInt(str, defaultVal);
//        return NumberUtils.toShort(str, defaultVal);
//        return NumberUtils.toLong(str, defaultVal);
//        return NumberUtils.toFloat(str, defaultVal);
//        return NumberUtils.toDouble(str, defaultVal);
//        return NumberUtils.toByte(str, defaultVal);
    }

    /**
     * 转浮点型
     *
     * @param str
     * @param defaultVal
     * @return
     */
    public static Double parseDouble(String str, Double defaultVal) {
        return NumberUtils.toDouble(str, defaultVal);
    }


}
