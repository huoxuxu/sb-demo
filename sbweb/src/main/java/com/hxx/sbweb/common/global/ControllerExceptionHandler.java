package com.hxx.sbweb.common.global;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-04-19 16:16:48
 **/
@Slf4j
@ControllerAdvice(basePackages = "com.hxx")
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResult<Object>handleControllerException(Exception e){
        log.error(ExceptionUtils.getStackTrace(e));
        return BaseResult.fail(500,e.getMessage());
    }

}

