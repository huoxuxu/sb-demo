package com.hxx.sbrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("log")
public class Slf4jTestController {

    @RequestMapping("/w")
    public String w() {
        log.info("请求参数：{} {{}}{{++--}}", 1, 2);
        log.info("请求参数：\\{} {{}}{{++--}}", 1);
        return 999 + "";
    }

}
