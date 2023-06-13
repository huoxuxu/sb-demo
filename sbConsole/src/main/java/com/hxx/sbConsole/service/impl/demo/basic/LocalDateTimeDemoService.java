package com.hxx.sbConsole.service.impl.demo.basic;

import java.time.Duration;
import java.time.LocalDateTime;
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
                    LocalDateTime st = LocalDateTime.parse("2023-06-05 00:00:00", df);
                    LocalDateTime et = LocalDateTime.parse("2023-06-12 00:00:00", df);

                    Duration dur = Duration.between(st, et);
                    long days = dur.toDays();// 7
                    long hours = dur.toHours();// 168
                    long minutes = dur.toMinutes();// 10080
                    long millis = dur.toMillis();// 604800000

                    long durSeconds = dur.getSeconds();// 604800
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
