package com.hxx.sbcommon.common;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 张文慧
 * @Description: HttpServletRequest帮助类
 * @Date: 2021-01-12 18:52
 **/
public class HttpServletRequestUtil {

    /***
     * 获取调用接口请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        params.put("_ip", getRemoteAddress(request));
        params.put("_charset", StandardCharsets.UTF_8);
        Set<Map.Entry<String, Object>> keSet = new HashMap<String, Object>(request.getParameterMap()).entrySet();
        for (Iterator<Map.Entry<String, Object>> itr = keSet.iterator(); itr.hasNext(); ) {
            Map.Entry<String, Object> e = itr.next();
            if ((e.getValue() instanceof String[])) {
                params.put(e.getKey(), ((String[]) e.getValue())[0]);
            } else if ((e.getValue() instanceof String)) {
                params.put(e.getKey(), e.getValue().toString());
            }
        }
        return params;
    }

    /**
     * 获取请求IP
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
