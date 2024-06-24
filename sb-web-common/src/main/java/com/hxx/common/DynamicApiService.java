package com.hxx.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

@Service
public class DynamicApiService {

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    public void registerDynamicApi(String path, DynamicApiTemplate api) {
        RequestMappingInfo mapping = RequestMappingInfo
                .paths(path)
                .methods(RequestMethod.GET) // Assuming GET method
                .build();
        Method method = getHandlerMethod(api);
        handlerMapping.registerMapping(mapping, api, method);
    }

    private Method getHandlerMethod(Object handler) {
        Method[] methods = handler.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("handleRequest")) {
                return method;
            }
        }
        return null;
    }

    public interface DynamicApiTemplate {
        void handleRequest(String data);
    }
//
//    @Component
//    public class DynamicApiImpl implements DynamicApiTemplate {
//        @Override
//        public void handleRequest(String data) {
//            System.out.println("Handling request with data: " + data);
//        }
//    }
}
