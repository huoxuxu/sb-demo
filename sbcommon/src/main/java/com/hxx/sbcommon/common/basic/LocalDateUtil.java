package com.hxx.sbcommon.common.basic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-10 15:47:41
 **/
public class LocalDateUtil {
    private final static DateTimeFormatter Default_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     *
     * @param text
     * @param pattern
     * @return
     */
    public static LocalDate parse(String text, String pattern) {
        return LocalDate.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     *
     * @param text
     * @return
     */
    public static LocalDate parse(String text) {
        return LocalDate.parse(text, Default_Formatter);
    }


}
