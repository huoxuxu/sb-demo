package com.hxx.sbweb.common.global;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.model.Result;
import com.hxx.sbweb.common.global.model.RestResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 对 Controller 返回的内容在 HttpMessageConverter 进行类型转换之前拦截
 *
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
        return true;
    }

    /**
     * 必须返回字符串，否则会报错
     *
     * @param body
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (body == null) {
            if (Objects.requireNonNull(methodParameter.getMethod())
                    .getReturnType() == String.class) {
                return JsonUtil.toJSON(Result.success(body));
            }

            return Result.success(null);
        }

        if (body instanceof Result) {
            return body;
        } else if (body instanceof String) {
            return JsonUtil.toJSON(Result.success(body));
        }

        //定义一个统一的返回类
        return Result.success(body);
    }
}
