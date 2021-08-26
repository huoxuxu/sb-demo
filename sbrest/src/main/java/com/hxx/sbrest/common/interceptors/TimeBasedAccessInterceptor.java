package com.hxx.sbrest.common.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 17:50:15
 **/
public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {
    private int openingTime;
    private int closingTime;
    private String mappingURL;//利用正则映射到需要拦截的路径

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    public void setMappingURL(String mappingURL) {
        this.mappingURL = mappingURL;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if (mappingURL == null || url.matches(mappingURL)) {
            LocalDateTime now1 = LocalDateTime.now();
            int now = now1.getHour();
            if (now < openingTime || now > closingTime) {
                request.setAttribute("msg", "注册开放时间：9：00-12：00");
                request.getRequestDispatcher("/msg.jsp").forward(request, response);
                return false;
            }
            return true;
        }
        return true;
    }


}
