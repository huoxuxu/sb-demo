package com.hxx.sbweb.common.global;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.model.ResultBean;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    public final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)// 拦截所有异常
    public ResultBean handle(Exception e) {

//        if (e instanceof NullAttrException) {
//            return ResultHandler.error(ResultEnum.NULL_ATTR);
//        }

        log.error("全局异常捕获:{}", ExceptionUtils.getStackTrace(e));

        return ResultHandler.error("未知异常，请联系管理员");
    }

}
