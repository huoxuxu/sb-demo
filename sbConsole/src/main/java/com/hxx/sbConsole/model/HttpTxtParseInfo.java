package com.hxx.sbConsole.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-06 15:01:07
 **/
@Data
public class HttpTxtParseInfo {

    // 全局注释
    private List<String> globalComments = new ArrayList<>();
    // 全局变量
    private Map<String, Object> globalVariateMap = new HashMap<>();

    // 接口
    private List<HttpTxtParseApiInfo> apiInfos = new ArrayList<>();

    @Data
    public static class HttpTxtParseApiInfo {
        // 注释
        private List<String> comments = new ArrayList<>();
        // 变量
        private Map<String, Object> variateMap = new HashMap<>();

        private String name;
        private String httpMethod;
        private String requestUrl;
        private String queryString;
        // 请求头
        private Map<String, Object> headerMap = new HashMap<>();

        // 请求体
        private List<String> formBody;
        // 多块表单数据
        private String multipartFormData;

    }
}
