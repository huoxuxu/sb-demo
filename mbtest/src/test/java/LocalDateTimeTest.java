import com.hxx.mbtest.MbtestApplication;
import com.hxx.mbtest.service.T1Service;
import com.hxx.sbcommon.common.LocalDateTimeUtil;
import com.hxx.sbcommon.common.LocalDateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


/*
now：2021-07-22T14:44:00.589

==============of 创建=================
创建时间：2019-01-01T02:03:45
创建时间：2019-10-01T23:03:45
创建时间：2019-12-01T02:03:45
==============of 创建 end=================

==============parse=================
字符串时间转换：2019-10-01T22:33:44.567
字符串时间转换：2019-01-01T22:33:44  2019-12-01T22:33:44
BASIC_ISO_DATE 字符串日期转换-指定格式：2019-01-02
ISO_DATE 字符串日期转换-指定格式：2019-01-02
ISO_LOCAL_DATE 字符串日期转换-指定格式：2019-01-02
yyyy-MM-dd 字符串日期转换-指定格式：2019-01-02  2019-12-02
==============parse end=================

==============format=================
当前时间：2019-10-01T23:03:45
ISO_LOCAL_DATE_TIME 格式化后：2019-10-01T23:03:45
ISO_LOCAL_DATE 格式化后：2019-10-01
ISO_LOCAL_TIME 格式化后：23:03:45
YYYY-MM-dd hh:mm:ss 格式化后：2019-10-01 11:03:45
yyyy-MM-dd HH:mm:ss 格式化后：2019-10-01 23:03:45
==============format end=================

==============Date 《》 LocalDateTime=================
Date 转换成 LocalDateTime：2021-07-22T14:44:00.593
LocalDateTime 转换成 Date：Thu Jul 22 14:44:00 CST 2021
==============Date 《》 LocalDateTime end=================

==============时间戳=================
当前时间转时间戳：1626936240593
时间戳转换成时间：2021-07-22T14:44:00.593
==============时间戳 end=================

**/

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-13 9:09:21
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MbtestApplication.class)
public class LocalDateTimeTest {

    @Test
    public void test() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now：" + now);
        // 创建时间：2019-10-01T02:03:45
        LocalDateTime ofTime = LocalDateTime.of(2019, 10, 1, 23, 3, 45);
        System.out.println("创建时间：" + ofTime);

        System.out.println();
        System.out.println("==============of 创建=================");
        {
            // 创建时间：2019-01-01T02:03:45
            LocalDateTime ofTime1 = LocalDateTime.of(2019, 1, 1, 2, 3, 45);
            System.out.println("创建时间：" + ofTime1);
            // 创建时间：2019-12-01T02:03:45
            LocalDateTime ofTime12 = LocalDateTime.of(2019, 12, 1, 2, 3, 45);
            System.out.println("创建时间：" + ofTime12);
        }
        System.out.println("==============of 创建 end=================");

        System.out.println();
        System.out.println("==============parse=================");
        {
            {
                LocalDateTime ldtf1 = LocalDateTime.parse("2019-10-01T22:33:44.567");
                System.out.println("字符串时间转换：" + ldtf1);
                LocalDateTime ldtf2 = LocalDateTime.parse("2019-01-01 22:33:44", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                LocalDateTime ldtf3 = LocalDateTime.parse("2019-12-01 22:33:44", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println("字符串时间转换：" + ldtf2 + "  " + ldtf3);
            }


        }
        System.out.println("==============parse end=================");

        System.out.println();
        System.out.println("==============format=================");
        {
            System.out.println("当前时间：" + ofTime);
            System.out.println("ISO_LOCAL_DATE_TIME 格式化后："
                    + ofTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            System.out.println("ISO_LOCAL_DATE 格式化后："
                    + ofTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
            System.out.println("ISO_LOCAL_TIME 格式化后："
                    + ofTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
            System.out.println("YYYY-MM-dd hh:mm:ss 格式化后："
                    + ofTime.format(DateTimeFormatter.ofPattern("YYYY-MM-dd hh:mm:ss")));
            System.out.println("yyyy-MM-dd HH:mm:ss 格式化后："
                    + ofTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        System.out.println("==============format end=================");

        System.out.println();
        System.out.println("==============Date<<<>>>LocalDateTime=================");
        {
            Date date = new Date();
            LocalDateTime ldt2date = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            System.out.println("Date >>> LocalDateTime：" + ldt2date);

            Date toDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            System.out.println("LocalDateTime >>> Date：" + toDate);
        }
        System.out.println("==============Date<<<>>>LocalDateTime end=================");

        // 当前时间转时间戳
        System.out.println();
        System.out.println("==============时间戳=================");
        {
            long epochMilli = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();
            System.out.println("当前时间转13位毫秒时间戳：" + epochMilli);

            // 13位毫秒时间戳转换成时间
            LocalDateTime epochMilliTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
            System.out.println("时间戳转换成时间：" + epochMilliTime);
        }
        System.out.println("==============时间戳 end=================");


    }


    @Test
    public void Run() {
        System.out.println("==============LocalDateTimeTest.Run==============");
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);
        // 第一天
        LocalDateTime firstDay = now.withDayOfMonth(1);
        System.out.println("本月第一天：" + firstDay);
        // 当天最后一秒
        LocalDateTime lastSecondOfDay = now.withHour(23).withMinute(59).withSecond(59);
        System.out.println("当天最后一秒：" + lastSecondOfDay);
        // 最后一天
        LocalDateTime lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("本月最后一天:" + lastDay);
        // 是否闰年
        System.out.println("今年是否闰年：" + Year.isLeap(now.getYear()));

        //获取当前时间月份的第几天
        System.out.println("本月第 n 天：" + now.getDayOfMonth());
        //获取当前周的第几天
        System.out.println("本周第 n 天：" + now.getDayOfWeek());
        //获取当前时间在该年属于第几天
        System.out.println("本年第 n 天：" + now.getDayOfYear());

        System.out.println("==============LocalDateTimeTest.Run END==============");

        /*
            当前时间：2021-07-08T16:04:33.933
            本月第一天：2021-07-01T16:04:33.933
            当天最后一秒：2021-07-08T23:59:59.933
            本月最后一天:2021-07-31T16:04:33.933
            今年是否闰年：false
            本月第 n 天：8
            本周第 n 天：THURSDAY
            本年第 n 天：189
        */
    }

    @Test
    public void Format() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("当前时间：" + now);
        System.out.println("DateTimeFormatter.ISO_LOCAL_DATE_TIME格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("DateTimeFormatter.ISO_LOCAL_DATE格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_DATE));
        System.out.println("DateTimeFormatter.ISO_LOCAL_TIME格式化后：" + now.format(DateTimeFormatter.ISO_LOCAL_TIME));
        System.out.println("yyyy-MM-dd HH:mm:ss 格式化后：" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("yyyy-MM-dd hh:mm:ss 格式化后：" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));

        /*
            当前时间：2021-07-08T16:08:45.930
            DateTimeFormatter.ISO_LOCAL_DATE_TIME格式化后：2021-07-08T16:08:45.93
            DateTimeFormatter.ISO_LOCAL_DATE格式化后：2021-07-08
            DateTimeFormatter.ISO_LOCAL_TIME格式化后：16:08:45.93
            yyyy-MM-dd HH:mm:ss 格式化后：2021-07-08 16:08:45
            yyyy-MM-dd HH:mm:ss 格式化后：2021-07-08 04:08:45
        */
    }

    @Test
    public void of() {
        {
            // 2015-06-21 13:40
            LocalDateTime localDateTime = LocalDateTime.of(2015, 6, 21, 13, 40);
            System.out.println(localDateTime);
        }
    }

    @Test
    public void atXXX() {
        LocalDate localDate = LocalDate.of(2014, 6, 21);
        System.out.println(localDate);

        LocalDateTime localTime1 = localDate.atStartOfDay();
        System.out.println(localTime1);

        LocalDateTime localTime2 = localDate.atTime(16, 21);
        System.out.println(localTime2);

        LocalDate localDate2 = Year.of(2014).atMonth(6).atDay(21);
        System.out.println(localDate2);
    }

    @Test
    public void nowXXX() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);

        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);

        LocalDateTime dateTime = LocalDateTime.now();
        System.out.println(dateTime);

        ZonedDateTime dateTimeWithZone = ZonedDateTime.now();
        System.out.println(dateTimeWithZone);
    }

    @Test
    public void ChronoUnitXXX() {
        LocalDateTime startTime = LocalDateTimeUtil.parse("2021-05-10 00:00:00");
        LocalDateTime endTime = LocalDateTimeUtil.parse("2021-08-10 00:00:00");
        long month1 = ChronoUnit.MONTHS.between(startTime, endTime);
        long day1 = ChronoUnit.DAYS.between(startTime, endTime);
        if (month1 > 3) {
            System.out.println("大于3个月");
        }

        startTime = LocalDateTimeUtil.parse("2021-05-09 00:00:00");
        long month2 = ChronoUnit.MONTHS.between(startTime, endTime);
        long day2 = ChronoUnit.DAYS.between(startTime, endTime);
        if (month2 > 3) {
            System.out.println("大于3个月");
        }
    }

    @Test
    public void DurationXXX() {
        LocalDateTime startTime = LocalDateTimeUtil.parse("2021-05-10 00:00:00");
        LocalDateTime endTime = LocalDateTimeUtil.parse("2021-08-10 00:00:00");
        Duration d1 = Duration.between(startTime, endTime);
        if (d1.toDays() > 3) {
            System.out.println("大于3个月");
        }

        startTime = LocalDateTimeUtil.parse("2021-05-09 00:00:00");
        Duration d2 = Duration.between(startTime, endTime);
        if (d2.toDays() > 3) {
            System.out.println("大于3个月");
        }
    }

}
