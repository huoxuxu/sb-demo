package com.hxx.sbConsole.commons.aspect;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 打印切面入参出参
 * 使用方法：需要的类打上：@PrintParamAnno
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-10-16 14:53:59
 **/
@Aspect
@Component
@Order(1000)
@Slf4j
public class PrintParamAspect {
    @Pointcut("@within(com.hxx.sbConsole.commons.annotation.PrintParamAnno)")
    public void objPointCut() {
    }

    @Pointcut("@annotation(com.hxx.sbConsole.commons.annotation.PrintParamAnno)")
    public void methodPointCut() {
    }

    @Around("objPointCut() || methodPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String uuid = OftenUtil.getUUID();
        String methodSign = getMethodSign(point);
        log.error("【参数打印】in[{}]，方法：{}，para：{}", uuid, methodSign, JsonUtil.toJSON(point.getArgs()));

        Object proceed = null;
        Throwable ex = null;
        try {
            proceed = point.proceed();
            return proceed;
        } catch (Throwable throwable) {
            ex = throwable;
            throw throwable;
        } finally {
            String errStr = ex == null ? "" : "[出现异常]，";
            log.error("【参数打印】out[{}]，方法：{}，{}para：{}", uuid, methodSign, errStr, JsonUtil.toJSON(proceed));
        }
    }

    // 获取方法签名
    public String getMethodSign(ProceedingJoinPoint point) {
        Signature signature = point.getSignature();
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

}
