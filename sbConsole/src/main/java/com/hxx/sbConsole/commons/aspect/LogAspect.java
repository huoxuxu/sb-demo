package com.hxx.sbConsole.commons.aspect;

import com.hxx.sbConsole.commons.annotation.LogAction;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 10:18:25
 **/
@Aspect
@Component
public class LogAspect {
    @Pointcut("@annotation(com.hxx.sbConsole.commons.annotation.LogAction)")
    public void annocationPointCut() {
    }

    @After("annocationPointCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();
        LogAction action = method.getAnnotation(LogAction.class);
        System.out.println(action.name());
    }

//    @Before("execution(* com.hxx.sbConsole.service.impl.DemoMethodService.*(..))")
    @Before("annocationPointCut()")
    // 前置通知, 在方法执行之前执行
    public void before(JoinPoint joinPoint) {
        MethodSignature sig = (MethodSignature) joinPoint.getSignature();
        Method method = sig.getMethod();
        System.out.println(method.getName());
    }

}
