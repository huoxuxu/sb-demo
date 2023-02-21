package com.hxx.sbrest.controller;

import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("bt")
public class BasicTestController {

    @Autowired
    private BasicTestService basicTestService;

    @RequestMapping("/switchTest")
    public String switchTest(String str) {
        log.info("请求参数：{} {{}}{{++--}}", str, "889");
        String str1 = basicTestService.switchTest(str);
        return str1;
    }

    @ResponseBody
    @ExceptionHandler({Exception.class, IllegalArgumentException.class})
    public String exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
//        return Result.error("SYSTEM_ERROR", "系统异常");
        return "系统异常";
    }

}
