package com.hxx.sbweb.controller.webDemo;

import com.hxx.sbcommon.model.Result;
import com.hxx.sbweb.common.JsonUtil;
import com.hxx.sbweb.model.UserModel;
import com.hxx.sbweb.model.enums.LinePatternEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试全局异常拦截
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/valid")
public class ValidParaController {

    @PostMapping("/model")
    public Result valid(@RequestBody @Validated UserModel u) {
        {
            int[] codes = LinePatternEnum.getCodes();
        }
        return Result.success(JsonUtil.toJSON(u));
    }


    /**
     * 参数校验错误
     *
     * @return Result
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Result handlerParamsException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getFieldError();
        if (fieldError != null) {
            if (!fieldError.isBindingFailure()) {
                String msg = fieldError.getDefaultMessage();
                return Result.failed(msg);
            }
        }
        log.error("模型校验异常：{}", ExceptionUtils.getStackTrace(ex));
        return Result.failed("入参校验异常");
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Result handlerBindException(BindException ex) {
        FieldError fieldError = ex.getFieldError();
        if (fieldError != null) {
            if (!fieldError.isBindingFailure()) {
                String msg = fieldError.getDefaultMessage();
                return Result.failed(msg);
            }
        }
        log.error("模型绑定异常：{}", ExceptionUtils.getStackTrace(ex));
        return Result.failed("入参校验异常");
    }
}
