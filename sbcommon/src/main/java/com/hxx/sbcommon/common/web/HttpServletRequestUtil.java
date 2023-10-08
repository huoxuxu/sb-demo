package com.hxx.sbcommon.common.web;

import com.hxx.sbcommon.common.io.StreamUtil;
import com.hxx.sbcommon.model.HttpMultipartFormDataInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author:
 * @Description: HttpServletRequest帮助类
 * @Date: 2021-01-12 18:52
 **/
public class HttpServletRequestUtil {

    /**
     * 获取请求头
     *
     * @param request
     * @return
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            String headerVal = request.getHeader(header);
            headerMap.put(header, headerVal);
        }
        return headerMap;
    }

    /**
     * 读取请求体
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) throws IOException {
        try (InputStream is = request.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }

    /**
     * 获取multipart数据
     *
     * @param request
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public static List<HttpMultipartFormDataInfo> getMultipartFormData(HttpServletRequest request) throws ServletException, IOException {
        List<HttpMultipartFormDataInfo> files = new ArrayList<>();
        String contentType = request.getContentType() + "";
        if (request instanceof StandardMultipartHttpServletRequest || contentType.toLowerCase().startsWith("multipart/formdata")) {
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                HttpMultipartFormDataInfo vo = new HttpMultipartFormDataInfo();
                {
                    vo.setFormName(part.getName());
                    vo.setContentType(part.getContentType());
                    String fileName = part.getSubmittedFileName();
                    vo.setFileName(fileName);
                    if (StringUtils.isBlank(fileName)) {
                        byte[] valBytes = StreamUtil.readInputStream(part.getInputStream(), (int) part.getSize(), true);
                        String val = new String(valBytes, StandardCharsets.UTF_8);
                        vo.setFormVal(val);
                    } else {
                        // 上传文件时
//                        vo.setInputStream(part.getInputStream());
                    }

                    Map<String, String> partHeaderMap = new HashMap<>();
                    Collection<String> headerNames = part.getHeaderNames();
                    for (String headerName : headerNames) {
                        String headerVal = part.getHeader(headerName);
                        partHeaderMap.put(headerName, headerVal);
                    }
                    vo.setHeaders(partHeaderMap);
                }
                files.add(vo);
            }
        }
        return files;
    }

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
