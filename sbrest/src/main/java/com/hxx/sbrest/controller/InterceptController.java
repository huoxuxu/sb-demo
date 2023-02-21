package com.hxx.sbrest.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:57:52
 **/
@Slf4j
@RestController
public class InterceptController {
    @RequestMapping("/index")
    public String index() {
        System.out.println("InterceptController/index");
        return "index";
    }

}
