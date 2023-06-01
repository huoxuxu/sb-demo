package com.hxx.sbConsole.service.impl.demo.basic;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-06-01 9:28:10
 **/
public class LocalDateTimeDemoService {
    public static void main(String[] args) {
        try {
            {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime st = now.minusSeconds(88);
                Duration dur = Duration.between(st, now);
                long durSeconds = dur.getSeconds();
                System.out.println(durSeconds);
            }
        } catch (Exception e) {
            System.out.printf(e + "");
        }
    }
}
