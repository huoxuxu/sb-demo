package com.hxx.sbcommon.common.basic.datetime;

import com.hxx.sbcommon.common.basic.OftenUtil;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-12-11 10:27:19
 **/
public class DateUtil {
    /**
     * 格式化为：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * 格式化为：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.parse(dateStr);
    }

    public static Date parseDateTime(String dateTimeStr) throws ParseException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.parse(dateTimeStr);
    }

    /**
     * 获取现在时间
     *
     * @return返回长时间格式 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在日期
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取两个时间间隔的天数，必须是精确到秒级的时间段
     * 使用时>=start && <=end
     *
     * @param start 开始时间
     * @param end   结束时间 必须带上23：59：59
     * @return
     */
    public static Long getIntervalDaysFix(Date start, Date end) {
        return getIntervalDaysFix(start, end, ChronoUnit.DAYS);
    }

    /**
     * 获取两个时间间隔数，必须是精确到秒级的时间段
     * 使用时>=start && <=end
     *
     * @param start
     * @param end
     * @param unit
     * @return
     */
    public static Long getIntervalDaysFix(Date start, Date end, ChronoUnit unit) {
        // 这样得到的差值是微秒级别
        long diff = end.getTime() + 1000 - start.getTime();

        int intervalMs = 1000;
        switch (unit) {
            case HOURS:
                intervalMs *= 60 * 60;
                break;
            case MINUTES:
                intervalMs *= 60;
                break;
            case SECONDS:
                break;
            case DAYS:
            default:
                intervalMs *= 60 * 60 * 24;
                break;
        }

        // 间隔天数
        long interval = diff / intervalMs;
        if (diff % intervalMs != 0) {
            interval++;
        }
        return interval;
    }

    /**
     * 向后滚动time毫秒的时间
     *
     * @param date
     * @param time 毫秒
     * @return
     */
    public static Date rollAfterDate(Date date, long time) {
        return new Date(date.getTime() + time);
    }

    /**
     * 校验开始结束时间
     *
     * @param begin
     * @param end
     * @param maxDay 不能超过的天数
     */
    public static void checkDate(Date begin, Date end, int maxDay) {
        if (begin == null || end == null) {
            throw new IllegalArgumentException("开始时间结束时间不能为空");
        }

        if (begin.after(end)) {
            throw new IllegalArgumentException("开始时间不能大于结束时间");
        }

        LocalDateTime beginDt = OftenUtil.DateTimeUtil.parse(begin);
        LocalDateTime endDt = OftenUtil.DateTimeUtil.parse(end);
        if (endDt.getHour() == 0 && endDt.getMinute() == 0 && endDt.getSecond() == 0) {
            endDt = endDt.plusDays(1).minusSeconds(1);
        }
        if (!OftenUtil.DateTimeUtil.check(beginDt, endDt, maxDay)) {
            throw new IllegalArgumentException("只能查询" + maxDay + "天以内数据");
        }
    }

}
