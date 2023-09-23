package com.hxx.sbweb.controller.base;

import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
     * 处理@RequestBody 校验失败的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class})
    public String methodArgumentNotValidExceptionHandler(HttpServletRequest req, org.springframework.web.bind.MethodArgumentNotValidException e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        BindingResult bindingResult = e.getBindingResult();
        FieldError fieldError = bindingResult.getFieldError();

        return "参数校验失败，请修改后重试[" + fieldError.getField() + ": " + fieldError.getDefaultMessage() + "]";
    }

    /**
     * 处理 @RequestParam 校验失败的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({javax.validation.ConstraintViolationException.class})
    public String constraintViolationExceptionHandler(HttpServletRequest req, javax.validation.ConstraintViolationException e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        return "参数校验失败，请修改后重试[" + e.getMessage() + "]";
    }

    /**
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public String missingServletRequestParameterExceptionHandler(HttpServletRequest req, MissingServletRequestParameterException e) {
        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
        return "参数校验失败，请修改后重试";
    }

//    /**
//     * 捕获：@PreAuthorize("@permissionService.hasPermission('XX_Access')")抛出的异常
//     *
//     * @param req
//     * @param e
//     * @return
//     */
//    @ResponseBody
//    @ExceptionHandler({org.springframework.security.access.AccessDeniedException.class})
//    public String accessDeniedExceptionHandler(HttpServletRequest req, Exception e) {
//        log.error("出现异常：{}", ExceptionUtils.getStackTrace(e));
//        return "您没有权限，请联系管理员";
//    }

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
