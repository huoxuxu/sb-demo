package com.hxx.sbConsole.service.impl.demo.db;

import com.hxx.sbConsole.service.common.LesseeServiceImpl;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-25 17:35:42
 **/
public class DemoDatabaseService {
    public static void main(String[] args) {
        try {
            demo();
            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public static void demo() {
        System.out.println("==================================================");
        {
            LesseeServiceImpl lesseeService = new LesseeServiceImpl(new LesseeServiceImpl.LesseeConfig("demo-db"));
            lesseeService.del();
        }
    }
}
