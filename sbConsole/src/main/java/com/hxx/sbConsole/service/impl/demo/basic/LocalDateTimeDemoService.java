package com.hxx.sbConsole.service.impl.demo.basic;

import com.hxx.sbcommon.common.basic.OftenUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-01 9:28:10
 **/
public class LocalDateTimeDemoService {
    private static final int MAX_DAY = 7;

    public static void main(String[] args) {
        try {
            {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                {
                    LocalDateTime st = LocalDateTime.parse("2023-06-11 00:00:00", df);
                    LocalDateTime et = LocalDateTime.parse("2023-06-11 23:59:59", df);

                    Duration dur = Duration.between(st, et);
                    long days = dur.toDays();// 0
                    long hours = dur.toHours();// 23
                    long minutes = dur.toMinutes();// 1469
                    long millis = dur.toMillis();// 86399000

                    long durSeconds = dur.getSeconds();// 86399
                    System.out.println(durSeconds);
                }
                {
                    LocalDateTime st = LocalDateTime.parse("2023-06-05 00:00:00", df);
                    LocalDateTime et = LocalDateTime.parse("2023-06-11 23:59:59", df);

                    Duration dur = Duration.between(st, et);
                    long days = dur.toDays();// 6
                    long hours = dur.toHours();// 167
                    long minutes = dur.toMinutes();// 10079
                    long millis = dur.toMillis();// 604799000

                    long durSeconds = dur.getSeconds();// 604799
                    System.out.println(durSeconds);
                }
                {
                    // 相差的总秒数
                    LocalDateTime st = OftenUtil.DateTimeUtil.parseDateTime("2023-06-05 00:00:00");
                    LocalDateTime et = OftenUtil.DateTimeUtil.parseDateTime("2023-06-05 23:59:59");

                    Duration dur = Duration.between(st, et);
                    long days = dur.toDays();// 0
                    long hours = dur.toHours();// 23
                    long minutes = dur.toMinutes();// 1439
                    // 相差总秒数
                    long durSeconds = dur.getSeconds();// 86399
                    long millis = dur.toMillis();// 86399000

                    System.out.println(durSeconds);
                }
                {
                    LocalDateTime st = LocalDateTime.parse("2023-06-05 00:00:00", df);
                    LocalDateTime et = LocalDateTime.parse("2023-06-12 00:00:00", df);
                    long curInterval = ChronoUnit.SECONDS.between(st, et);// 604800
                    System.out.println(curInterval);
                }
                {
                    LocalDateTime st = LocalDateTime.parse("2023-06-05 00:00:00", df);
                    LocalDateTime et = LocalDateTime.parse("2023-06-11 23:59:59", df);
                    long curInterval = ChronoUnit.SECONDS.between(st, et);// 604799
                    System.out.println(curInterval);
                }
            }
        } catch (Exception e) {
            System.out.printf(e + "");
        }
    }
}
