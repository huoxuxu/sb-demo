package com.hxx.sbweb.common.global;

import com.hxx.sbweb.common.ResultHandler;
import com.hxx.sbweb.model.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常处理类的调用优先级是直接按照包结构从上到下顺序调用
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-07 15:15:23
 **/
@Slf4j
@ControllerAdvice(assignableTypes= com.hxx.sbweb.controller.demo.GlobalErrController.class)
@ResponseBody
public class AGlobalIOExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler({IOException.class})
//    public ResultBean exceptionHandler(IOException e) {
//        log.error("全局IO异常捕获:{}", ExceptionUtils.getStackTrace(e));
//
//        return ResultHandler.error("未知异常，请联系管理员");
//    }

    @ResponseBody
    @ExceptionHandler({IOException.class})
    public String exceptionHandler(final HttpServletRequest request, final HttpServletResponse response, IOException e) {
        log.error("全局IO异常捕获:{}", ExceptionUtils.getStackTrace(e));

        return "123321";
    }

}
