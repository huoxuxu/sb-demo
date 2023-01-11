package com.hxx.sbConsole.thread.threadLocal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-06 9:30:24
 **/
@Slf4j
@Service
public class ThreadLocalServiceDemo {
    public static void main(String[] args) {
        String str="1,2,3,";
        String[] split = str.split(",");
        System.out.println(split.length);
        System.out.println(String.join(",",split));

    }
}
