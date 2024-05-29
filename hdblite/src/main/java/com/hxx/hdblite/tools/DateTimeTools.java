package com.hxx.hdblite.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeTools {

    //yyyy-MM-dd'T'hh:mm:ss
    //yyyy-MM-dd HH:mm:ss
    public static void Test1() {
        String dateString = "2013-02-12T12:22:33";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        System.out.println("simpleDate format " + simpleDateFormat.format(new Date()));
        try {
            System.out.println("simpleDate parse " + simpleDateFormat.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将日期格式化为yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String ToStr(Date date) {
        return ToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param formatStr yyyy-MM-dd HH:mm:ss等
     * @return
     */
    public static String ToStr(Date date, String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);

        return simpleDateFormat.format(date);
    }

    /**
     * 只支持解析：yyyy-MM-dd HH:mm:ss的日期字符串
     *
     * @param dateStr
     * @return
     */
    public static Date ToDateTime(String dateStr) throws Exception {
        return ToDateTime(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 解析指定格式化日期字符串
     *
     * @param dateStr
     * @param formatStr yyyy-MM-dd HH:mm:ss等
     * @return
     * @throws Exception
     */
    public static Date ToDateTime(String dateStr, String formatStr) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);

        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 获取UTC时间（13）
     *
     * @return
     */
    public static long ToUTC() {
        {
//        // 取得本地时间：
//        Calendar cal = Calendar.getInstance();
//        // 取得时间偏移量：
//        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
//        // 取得夏令时差：
//        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
//
//        // 从本地时间里扣除这些差量，即可以取得UTC时间：
//        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
//
//        long mills = cal.getTimeInMillis();
//        //System.out.println("UTC = " + mills);
//
//        return mills;
        }

        Calendar cal = Calendar.getInstance();
        return cal.getTimeInMillis();
    }

    public static Date UTC2DateTime(long millis) {
        {
//            // 取得本地时间：
//            Calendar cal = Calendar.getInstance();
//            // 取得时间偏移量：
//            int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
//            // 取得夏令时差：
//            int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
//
//            cal.setTimeInMillis(millis);
//
//            SimpleDateFormat foo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String time = foo.format(cal.getTime());
//            //System.out.println("GMT time= " + time);
//
//            // 从本地时间里扣除这些差量，即可以取得UTC时间：
//            cal.add(java.util.Calendar.MILLISECOND, (zoneOffset + dstOffset));
//            time = foo.format(cal.getTime());
//            //System.out.println("Local time = " + time);
//            return time;
        }

        return new Date(millis);
    }

    /**
     * 比较d2与d1之差
     *
     * @param date1 格式为yyyy-MM-dd HH:mm:ss
     * @param date2 格式为yyyy-MM-dd HH:mm:ss
     * @return d2-d1的毫秒数
     */
    public static long compare(Date date1, Date date2) {
        long diff = 0;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(date1);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date2);

        diff = c2.getTimeInMillis() - c1.getTimeInMillis();
        return diff;
    }

}
