package com.hxx.sbweb.common;

import com.hxx.sbweb.consts.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期辅助工具
 *
 * @author xiyunfei
 * @date 2021/01/22
 */
public class DateUtil {

    /**
     * 添加秒
     *
     * @param time      指定时间
     * @param addSecond 增加时间（单位：秒）
     * @return 新时间
     */
    public static Date addSecond(Date time, int addSecond) {
        return addTime(time, Calendar.SECOND, addSecond);
    }

    /**
     * 增加分钟
     *
     * @param time      指定时间
     * @param addMinute 增加时间（单位：分钟）
     * @return 新时间
     */
    public static Date addMinute(Date time, int addMinute) {
        return addTime(time, Calendar.MINUTE, addMinute);
    }

    /**
     * 增加小时
     *
     * @param time    指定时间
     * @param addHour 增加时间（单位：小时）
     * @return 新时间
     */
    public static Date addHour(Date time, int addHour) {
        return addTime(time, Calendar.HOUR, addHour);
    }

    /**
     * 增加天数
     *
     * @param time   指定时间
     * @param addDay 增加时间（单位：天）
     * @return 新时间
     */
    public static Date addDay(Date time, int addDay) {
        return addTime(time, Calendar.DATE, addDay);
    }

    /**
     * 增加月份
     *
     * @param time     指定时间
     * @param addMonth 增加时间（单位：月）
     * @return 新时间
     */
    public static Date addMonth(Date time, int addMonth) {
        return addTime(time, Calendar.MONTH, addMonth);
    }

    /**
     * 增加年份
     *
     * @param time    指定时间
     * @param addYear 增加时间（单位：年）
     * @return 新时间
     */
    public static Date addYear(Date time, int addYear) {
        return addTime(time, Calendar.YEAR, addYear);
    }

    /**
     * 增加时间
     *
     * @param time    指定时间
     * @param kind    类型
     * @param addTime 增加时间
     */
    private static Date addTime(Date time, int kind, int addTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);   //设置时间
        c.add(kind, addTime); //日期分钟加1,Calendar.DATE(天),Calendar.HOUR(小时)
        return c.getTime(); //结果
    }

    /**
     * yyyy-MM-dd hh:mm:ss 类型的日期返回date类型
     *
     * @param date 时间字符
     * @return 日期时间
     */
    public static Date getDateFromStr(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(date);
        } catch (Exception e) {
            //log.error("时间{}格式化错误", date, e);
            return null;
        }
    }

    /**
     * 兼容 yyyy-MM-dd hh:mm:ss 和 yyyy-MM-dd 类型的日期返回date类型
     *
     * @param date    日期
     * @param formStr 字符格式
     * @return 日期时间
     */
    public static Date getDateFromStr2(String date, String formStr) throws ParseException {
        List<SimpleDateFormat> formats = new ArrayList<>();
        formats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        if (!StringUtils.isEmpty(formStr)) {
            formats.add(new SimpleDateFormat(formStr));
        }
        for (SimpleDateFormat sdf : formats) {
            try {
                return sdf.parse(date);
            } catch (Exception ex) {
            }
        }
        throw new ParseException("时间格式化错误:" + date, 0);
    }

    /**
     * 时间戳转为日期格式
     *
     * @param stamp 时间戳
     * @return 日期时间
     */
    public static Date getDateFromStamp(String stamp) {
        try {
            return new Date(Long.parseLong(stamp));
        } catch (Exception e) {
            //log.error("时间{}格式化错误", stamp, e);
            return null;
        }
    }

    /**
     * 返回yyyy-mm-dd hh:mm:ss 类型的日期
     *
     * @param date 日期
     * @return 日期时间
     */
    public static String getStrFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 根据时间字符返回时间
     *
     * @param dateStr   日期时间
     * @param formatStr 默认日期格式 yyyy-MM-dd HH:mm:ss
     * @return 当日期字符为null返回最小时间，否则返回日期
     */
    public static Date getDateFromStr(String dateStr, String formatStr) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return new Date(0);
        }
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.parse(dateStr);
    }

    /**
     * 返回yyyyMMdd类型的日期
     *
     * @param date 时间
     * @return 日期时间
     */
    public static String getSimpleStrFromDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }

    /**
     * 格式化时间字符（默认自带 yyyy-MM-dd HH:mm:ss）
     *
     * @param date
     * @param formatStr
     * @return
     * @throws ParseException
     */
    public static String getFormatStrFormDate(Date date, String formatStr) {
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    /**
     * 向后滚动time毫秒的时间
     *
     * @param date 日期
     * @param time 时间
     * @return
     */
    public static Date rollDate(Date date, long time) {
        long before = date.getTime();
        return new Date(before + time);
    }

    /**
     * @return
     */
    public static String getStrDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(currentTime);
    }

    /**
     * 获得某天最小时间  00:00:00
     *
     * @param date 日期
     * @return 时间
     */
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取当前时间分钟前n分钟
     *
     * @return 日期时间
     */
    public static Date getCurrentTimeAgo(int n) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar beforeTime = Calendar.getInstance();
        // 10分钟之前的时间
        beforeTime.add(Calendar.MINUTE, n);
        Date beforeD = beforeTime.getTime();
        String time = sdf.format(beforeD);
        return getDateFromStr(time);
    }

    /**
     * 根据时间戳获取执行格式时间
     *
     * @param timestamp 时间戳
     * @param format    默认格式 yyyy-MM-dd HH:mm:ss (参考：DateUtil.FORMAT_ONE)
     * @return 指定格式时间字符串
     */
    public static String getSpecifiedFormatDate(Long timestamp, String format) {
        if (StringUtils.isEmpty(format)) {
            format = Constant.FORMAT_ONE;
        }
        Calendar calendar = Calendar.getInstance();
        if (timestamp == null) {
            return new SimpleDateFormat(format).format(calendar.getTime());
        }
        calendar.setTimeInMillis(timestamp);
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

    /**
     * 根据时间字段返回时间
     *
     * @param dateStr   日期时间
     * @param formatStr 默认日期格式 yyyy-MM-dd HH:mm:ss
     * @return
     * @throws ParseException 时间格式转换异常
     */
    public static String getDateStrFromStr(String dateStr, String formatStr) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = sdf.parse(dateStr);
        if (StringUtils.isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        sdf = new SimpleDateFormat(formatStr);
        return sdf.format(dt);
    }

    /**
     * 格式化时间字符串同时兼容
     *
     * @param dateStr   日期时间
     * @param formatStr 默认日期格式 yyyy-MM-dd HH:mm:ss 兼容 yyyy-MM-dd
     * @return 返回指定格式日期字符串
     */
    public static String getDateStrFromStr2(String dateStr, String formatStr) {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        List<SimpleDateFormat> sdf = new ArrayList<>();
        sdf.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        sdf.add(new SimpleDateFormat("yyyy-MM-dd"));

        for (SimpleDateFormat item : sdf) {
            try {
                Date dt = item.parse(dateStr);
                if (StringUtils.isEmpty(formatStr)) {
                    formatStr = "yyyy-MM-dd HH:mm:ss";
                }
                SimpleDateFormat sd = new SimpleDateFormat(formatStr);
                return sd.format(dt);
            } catch (Exception ex) {
                //log.error("时间格式{}转换字符串{}异常", formatStr, dateStr);
            }
        }
        return null;
    }
}
