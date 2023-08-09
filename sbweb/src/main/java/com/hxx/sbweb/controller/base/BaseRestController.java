package com.hxx.sbweb.controller.base;

import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 基础RestController
 * 继承此类后，可以捕获Controller中的异常
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
public class BaseRestController {

    /**
     * 局部异常捕获，仅捕获当前Controller内的异常
     *
     * @param req httpRequet
     * @param e   exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler({com.fasterxml.jackson.databind.exc.InvalidFormatException.class, org.springframework.http.converter.HttpMessageNotReadableException.class})
    public String invalidFormatExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
//        return JsonUtil.toJSON(Result.error("SYSTEM_ERROR", "数据映射异常,请检查请求数据"));
        return "数据映射异常,请检查请求数据";
    }

    @ResponseBody
    @ExceptionHandler({Exception.class})
    public String exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
//        return JsonUtil.toJSON(Result.error("SYSTEM_ERROR", "请求失败,请重试或联系管理员"));
        return "请求失败,请重试或联系管理员";
    }

}
