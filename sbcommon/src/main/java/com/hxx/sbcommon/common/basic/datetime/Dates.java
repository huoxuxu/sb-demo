package com.hxx.sbcommon.common.basic.datetime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-26 16:06:55
 **/
public class Dates {
    /**
     * 获取日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date of(int year, int month, int day) {
        return new Date(year - 1900, month - 1, day);
    }

    /**
     * 获取日期
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date of(int year, int month, int day, int hour, int minute, int second) {
        return new Date(year - 1900, month - 1, day, hour, minute, second);
    }

    /**
     * 获取日期
     *
     * @param dateStr yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static Date ofDate(String dateStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateStr);
    }

    /**
     * 获取日期
     *
     * @param dateTimeStr yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException
     */
    public static Date ofDateTime(String dateTimeStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateTimeStr);
    }

    /**
     * 获取纳秒
     *
     * @return
     */
    public static long getNano() {
        Instant instant = Instant.now();
        return instant.getNano();
    }

    /**
     * 获取毫秒
     *
     * @return
     */
    public static long getMillisecond() {
        Instant instant = Instant.now();
        // 获取毫秒
        return instant.toEpochMilli();
    }


}
