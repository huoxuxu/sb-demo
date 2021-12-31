package com.hxx.sbweb.common.aspect;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/*
Spring使用AspectJ注解来声明通知方法
@Before  通知方法还会在目标方法调用之前执行
@Around 通知方法会将目标方法封装起来
@AfterThrowing 通知方法会在目标方法抛出异常后调用
@AfterReturning 通知方法会在目标方法返回后调用
@After 通知方法会在目标方法返回或抛出异常后调用


*/
/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-12-20 12:59:13
 **/
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.hxx.sbweb.common.annotation.OperationLog)")
    public void restControllerLog() {
    }

    /**
     * 需要忽略的返回日志
     */
    @Pointcut("!@annotation(com.hxx.sbweb.common.annotation.IgnoreResponseLog)")
    public void ignoreResponseLog() {
    }

    @Before("restControllerLog()")
    public void exBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        LogModel logModel = new LogModel();
        logModel.setUrl(request.getRequestURL().toString());
        logModel.setReqIp(request.getRemoteAddr());
        logModel.setStartTime(LocalDateTime.now());

        Signature signature = joinPoint.getSignature();
        logModel.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());
        try {
            logModel.setParams(getRequestParamsByJoinPoint(joinPoint));
            log.info("--------------请求参数日志------------");
            log.info(" ---> 请求参数 :{}", JSON.toJSONString(logModel));
        } catch (Exception e) {
            log.info("异常信息：{}", ExceptionUtils.getStackTrace(e));
        }
    }

    @After("restControllerLog()")
    public void exAfter(JoinPoint joinPoint) {
        log.info(" ---> request method:" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + " 执行完毕!");
    }

    @AfterReturning(returning = "result", pointcut = "restControllerLog()  && ignoreResponseLog()")
    public void exAfterReturning(Object result) {
        log.info("--------------请求结果日志------------");
        log.info(" ---> 请求结果 :{}", JSON.toJSONString(result));
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        //参数名
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        //参数值
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            //如果是文件对象
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                //获取文件名
                value = file.getOriginalFilename();
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

    @Data
    public static class LogModel {

        private String url;

        private LocalDateTime startTime;

        private LocalDateTime endTime;

        private Long tookTime;

        private Object result;

        private String reqIp;

        private Boolean success = Boolean.FALSE;

        private String errMsg;

        private Object queryString;

        private String method;

        private Map<String, Object> params;

        private RuntimeException exception;

    }

}
