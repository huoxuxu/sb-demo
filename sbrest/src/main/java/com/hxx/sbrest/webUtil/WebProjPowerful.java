package com.hxx.sbrest.webUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-07-21 15:17:24
 **/
@Slf4j
@Service
public class WebProjPowerful {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * 获取所有Controller中的Url
     *
     * @return
     */
    public Set<String> getAllUrl() {
        // url与方法的对应关系
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        Set<String> urls = new HashSet<>();
        handlerMethods.keySet().forEach(handlerMethod ->
                urls.addAll(handlerMethod.getPatternsCondition().getPatterns())
        );
        return urls;
    }
}
