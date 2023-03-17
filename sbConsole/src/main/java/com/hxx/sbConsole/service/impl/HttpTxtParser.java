package com.hxx.sbConsole.service.impl;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.io.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Token：Title、EmbedPara、HttpMethod、HttpUrl、HttpHeader、HttpRequestBody
 * 解析规则：
 * 以###分割接口信息，###后为接口名，###换行后可为 声明内嵌参数行或请求信息行
 * 内嵌参数以@开头，语法：@id=1 使用时{{id}}
 * 请求信息行以 HTTP请求方法开头，空格隔开请求URL
 * 请求URL中可带有内嵌参数
 * 请求信息行换行后为请求头
 * 请求头语法格式：Content-Type：application/json
 * 请求头换行后为请求体
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-17 13:59:14
 **/
public class HttpTxtParser {
    private File file;

    // 开始解析 ###
    private boolean startTitle;
    private boolean startEmbedParaOrRequestBody;
    private boolean startHttpHeader;
    private boolean startRequestBodyOrTitle;
    private HttpTxtInfo info = new HttpTxtInfo();

    public HttpTxtParser(File file) {
        this.file = file;
    }

    public List<HttpTxtInfo> parse() throws IOException {
        Set<String> methodSet = new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        List<HttpTxtInfo> ls = new ArrayList<>();
        startTitle = true;
        List<String> requestBodys = new ArrayList<>();

        FileUtil.readLines(this.file, StandardCharsets.UTF_8, line -> {
            if (startTitle) {
                if (StringUtils.isBlank(line)) {
                    // 跳过空行
                    return;
                }

                line = line.trim();
                parseTitle(line);
            }
            // 开始解析标头后的数据
            else {
                // 内嵌参数行
                if (startEmbedParaOrRequestBody) {
                    if (StringUtils.isBlank(line)) {
                        throw new IllegalStateException("非法空行！");
                    }

                    line = line.trim();
                    if (line.startsWith("@")) {
                        String[] arr = OftenUtil.StringUtil.splitFirst(line, "=");
                        info.getEmbedParaMap()
                                .put(arr[0], arr[1]);
                    }
                    // 不包含@ Http信息行
                    else {
                        // 解析Http信息
                        String[] arr = StringUtils.split(line, " ");
                        if (methodSet.contains(arr[0])) {
                            info.setMethod(arr[0]);
                            if (arr.length > 1) {
                                info.setUrl(arr[1]);

                                startEmbedParaOrRequestBody = false;
                                startHttpHeader = true;
                            } else {
                                // 格式不正确
                                throw new IllegalStateException("格式不正确，请求信息的Url不能包含空格");
                            }
                        } else {
                            // 格式不正确
                            throw new IllegalStateException("格式不正确，表头后必须是内嵌参数或请求信息");
                        }
                    }
                }
                // 非内嵌行 非Http信息行 HttpHeader行
                else if (startHttpHeader) {
                    if (StringUtils.isBlank(line)) {
                        // 空行说明下一个要读取RequestBody或者末尾了
                        startHttpHeader = false;
                        startRequestBodyOrTitle = true;
                        return;
                    }

                    line = line.trim();
                    info.getHeaders()
                            .add(line);
                }
                // 解析HttpBody
                else if (startRequestBodyOrTitle) {
                    if (StringUtils.isBlank(line)) {
                        // 空行说明读到RequestBody结尾了
                        info.setRequestBody(StringUtils.join(requestBodys, "\n"));
                        startRequestBodyOrTitle = false;
                        startTitle = true;

                        ls.add(info);
                        // 重置
                        info = new HttpTxtInfo();
                        return;
                    }
                    if (line.trim()
                            .startsWith("###")) {
                        // 开始解析Title
                        startRequestBodyOrTitle = false;
                        startTitle = true;

                        ls.add(info);
                        // 重置
                        info = new HttpTxtInfo();

                        parseTitle(line);
                    } else {
                        requestBodys.add(line);
                    }
                } else {
                    throw new IllegalStateException("非法状态！");
                }
            }
        });
        if (!StringUtils.isBlank(info.getName())) {
            ls.add(info);
        }
        return ls;
    }

    private void parseTitle(String line) {
        if (line.startsWith("###")) {
            if (line.length() == 3) {
                info.setName("未命名");
            } else {
                info.setName(line.substring(3));
            }
            startTitle = false;
            startEmbedParaOrRequestBody = true;
        } else {
            // 非标头，跳过
            return;
        }
    }

    @lombok.Data
    public static class HttpTxtInfo {
        /**
         * 名称
         */
        private String name;
        /**
         * GET POST PUT DELETE
         */
        private String method;
        /**
         * 请求地址
         */
        private String url;
        /**
         * 内嵌参数Map
         */
        private Map<String, Object> embedParaMap = new HashMap<>();
        /**
         * 请求头
         */
        private List<String> headers = new ArrayList<>();
        /**
         * 请求体
         */
        private String requestBody;
    }

}
