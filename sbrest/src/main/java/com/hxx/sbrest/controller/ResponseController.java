package com.hxx.sbrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 响应测试
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-10 14:53:42
 **/
@Slf4j
@RestController
@RequestMapping("resp")
public class ResponseController {

    /**
     * /resp/int
     *
     * @return
     */
    @GetMapping(value = "int")
    public int respInt() {
        return 101;
    }

    @GetMapping(value = "double")
    public double respDouble() {
        return 101.002D;
    }

    /**
     * /resp/object
     *
     * @return
     */
    @GetMapping(value = "object")
    public Object respObject() {
        // 如果返回字符串，那么包装后也必须返回字符串
        return "demo1str";
    }


}
