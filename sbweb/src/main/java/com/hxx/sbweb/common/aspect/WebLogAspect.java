//package com.hxx.sbweb.common.aspect;
//
///**
// * @Author: huoxuxu
// * @Description:
// * @Date: 2022-01-19 17:22:13
// **/
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//
///**
// * Web层日志切面
// *
// */
//@Slf4j
//@Aspect
//@Component
//public class WebLogAspect {
//
//    ThreadLocal<Long> startTime = new ThreadLocal<>();
//
//    /**
//     *
//     */
//    //表示匹配所有方法
//    //execution(* *(..))
//    // 表示匹配com.savage.server.UserService中所有的公有方法
//    //execution(public * com. savage.service.UserService.*(..))
//    // 表示匹配com.savage.server包及其子包下的所有方法
//    //execution(* com.savage.server..*.*(..))
//    // 某个package下的所有函数
//    @Pointcut("execution(public * com.hxx.sbweb.controller..*.*(..))")
//    public void webLog() {
//    }
//
//    @Before("webLog()")
//    public void doBefore(JoinPoint joinPoint) throws Throwable {
//        startTime.set(System.currentTimeMillis());
//
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//
//        // 记录下请求内容
//        log.info("URL : " + request.getRequestURL().toString());
//        log.info("HTTP_METHOD : " + request.getMethod());
//        log.info("IP : " + request.getRemoteAddr());
//        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        log.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
//
//    }
//
//    @AfterReturning(returning = "ret", pointcut = "webLog()")
//    public void doAfterReturning(Object ret) throws Throwable {
//        // 处理完请求，返回内容
//        log.info("RESPONSE : " + ret);
//        log.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
//    }
//
//
//}
