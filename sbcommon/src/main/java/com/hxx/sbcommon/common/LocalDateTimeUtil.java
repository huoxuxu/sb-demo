package com.hxx.sbcommon.common;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*
-- 比大小
endTime.isBefore(startTime)
-- 相差
long hour1 = ChronoUnit.DAYS.between(startTime, endTime);

*/

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-10 16:59:30
 **/
public class LocalDateTimeUtil {
    // yyyy-MM-dd HH:mm:ss
    private final static DateTimeFormatter Default_Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * @param year
     * @param monthOfYear    1-12
     * @param dayOfMonth     1-28\29\30\31
     * @param hourOfDay
     * @param minuteOfHour
     * @param secondOfMinute
     * @return
     */
    public static LocalDateTime create(int year, int monthOfYear, int dayOfMonth,
                                       int hourOfDay, int minuteOfHour, int secondOfMinute) {
        return LocalDateTime.of(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute);
    }

    public static LocalDateTime create(int year, int monthOfYear, int dayOfMonth,
                                       int hourOfDay, int minuteOfHour, int secondOfMinute,
                                       int millisOfSecond) {
        return LocalDateTime.of(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }

    /**
     * @param dateTime
     * @param pattern  yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * @param dateTime
     * @return
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime.format(Default_Formatter);
    }

    /**
     * @param text
     * @param pattern yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime parse(String text, String pattern) {
        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime parse(String text) {
        return LocalDateTime.parse(text, Default_Formatter);
    }

    /**
     * @param timestamp 转13位毫秒时间戳
     * @return
     */
    public static LocalDateTime parse(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * @param date Date
     * @return
     */
    public static LocalDateTime parse(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * 转13位毫秒时间戳
     *
     * @param dt
     * @return
     */
    public static long toTimestamp(LocalDateTime dt) {
        return dt.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }

    /**
     * 转Date
     *
     * @param dt
     * @return
     */
    public static Date toDate(LocalDateTime dt) {
        return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 转字符串
     *
     * @param dateTime
     * @return
     */
    public static String toString(LocalDateTime dateTime) {
        return format(dateTime);
    }

}
