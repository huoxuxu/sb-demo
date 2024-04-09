package com.hxx.sbweb.common.global;

import com.hxx.sbcommon.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常捕获
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)// 拦截所有异常
    public Result handle(Exception e) {
        log.error("全局异常捕获:{}", ExceptionUtils.getStackTrace(e));

//        if (e instanceof NullAttrException) {
//            return ResultHandler.error(ResultEnum.NULL_ATTR);
//        }

        if (e instanceof org.springframework.web.HttpRequestMethodNotSupportedException) {
            return Result.failed("HTTP请求方式错误，请联系管理员!");
        }


        return Result.failed("未知异常，请联系管理员");
    }

}
