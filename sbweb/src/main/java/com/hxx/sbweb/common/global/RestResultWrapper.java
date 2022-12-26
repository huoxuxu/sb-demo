package com.hxx.sbweb.common.global;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbweb.common.global.model.RestResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-12-16 10:15:27
 **/
// 此注解针对controller层的类做增强功能，即对加了@RestController注解的类进行处理
@ControllerAdvice(annotations = RestController.class)
public class RestResultWrapper implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果返回false，则 beforeBodyWrite 不被调用
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<?
            extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //定义一个统一的返回类
        RestResult responseResult = new RestResult(0, body, "success");
        //封装后的数据返回到前端页面
        return JsonUtil.toJSON(responseResult);
    }
}
