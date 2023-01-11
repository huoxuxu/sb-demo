package com.hxx.sbConsole.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 15:02:24
 **/
@Slf4j
public class ThreadDemo {

    public static void demo() {
        try {
            // 新开线程
            {
                new Thread(() -> System.out.println("123321")).start();
            }
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }
}
