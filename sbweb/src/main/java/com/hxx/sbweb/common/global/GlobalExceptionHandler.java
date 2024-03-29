package com.hxx.sbweb.common.global;

import com.hxx.sbcommon.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)// 拦截所有异常
    public Result handle(Exception e) {

//        if (e instanceof NullAttrException) {
//            return ResultHandler.error(ResultEnum.NULL_ATTR);
//        }

        log.error("全局异常捕获:{}", ExceptionUtils.getStackTrace(e));

        return Result.failed("未知异常，请联系管理员");
    }

}
