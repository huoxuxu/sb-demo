package com.hxx.sbConsole.module.httpTxtParser;

import com.hxx.sbcommon.common.basic.OftenUtil;
import com.hxx.sbcommon.common.basic.text.StringUtil;
import com.hxx.sbcommon.common.io.fileOrDir.FileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
    private final static Set<String> methodSet = new HashSet<>(Arrays.asList("GET", "POST", "PUT", "DELETE"));

    private File file;
    private Charset charset = StandardCharsets.UTF_8;

    // 开始解析 ###
    private boolean startTitleToken;
    private boolean startEmbedParaOrRequestBodyToken;
    private boolean startHttpHeaderToken;
    private boolean startRequestBodyOrTitleToken;
    private HttpTxtInfo info = new HttpTxtInfo();

    /**
     * 默认使用UTF8
     *
     * @param file
     */
    public HttpTxtParser(File file) {
        this.file = file;
    }

    /**
     * @param file
     * @param charset
     */
    public HttpTxtParser(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    /**
     * 序列化为字符串
     *
     * @param infos
     * @return
     */
    public static String writeTo(List<HttpTxtInfo> infos) {
        List<String> ls = new ArrayList<>();
        for (HttpTxtInfo info : infos) {
            StringBuilder sb = new StringBuilder();
            sb.append("### ");
            sb.append(info.getName());
            sb.append('\n');

            Map<String, String> embedParaMap = info.getEmbedParaMap();
            if (embedParaMap != null && !embedParaMap.isEmpty()) {
                for (Map.Entry<String, String> entry : embedParaMap.entrySet()) {
                    sb.append("@");
                    sb.append(entry.getKey());
                    sb.append('=');
                    sb.append(entry.getValue());
                    sb.append('\n');
                }
            }
            sb.append(info.getMethod());
            sb.append(" ");
            sb.append(info.getUrl());
            sb.append('\n');

            if (!CollectionUtils.isEmpty(info.getHeaders())) {
                for (String header : info.getHeaders()) {
                    sb.append(header);
                    sb.append('\n');
                }
                sb.append('\n');
            }

            if (!StringUtils.isBlank(info.getRequestBody())) {
                sb.append(info.getRequestBody());
                sb.append('\n');
            }

            ls.add(sb.toString());
        }
        return StringUtils.join(ls, "\n");
    }

    public List<HttpTxtInfo> parse() throws IOException {
        List<HttpTxtInfo> ls = new ArrayList<>();
        startTitleToken = true;
        info = new HttpTxtInfo();
        List<String> requestBodys = new ArrayList<>();

        FileUtil.readLines(this.file, this.charset, line -> {
            // Title
            if (startTitleToken) {
                if (StringUtils.isBlank(line)) {
                    // 跳过空行
                    return;
                }

                line = line.trim();
                parseTitle(line);
            }
            // Title 后
            else {
                // 内嵌参数行
                if (startEmbedParaOrRequestBodyToken) {
                    if (StringUtils.isBlank(line)) {
                        throw new IllegalStateException("非法空行！");
                    }

                    line = line.trim();
                    if (line.startsWith("@")) {
                        String[] arr = StringUtil.splitFirst(line, "=");
                        info.getEmbedParaMap()
                                .put(StringUtil.trimStart(arr[0], "@"), arr[1]);
                    }
                    // 不包含@ Http信息行
                    else {
                        // 解析Http信息
                        parseHttpInfo(line);
                    }
                }
                // 非内嵌行 非Http信息行 HttpHeader行
                else if (startHttpHeaderToken) {
                    if (StringUtils.isBlank(line)) {
                        // 空行说明下一个要读取RequestBody或者末尾了
                        startHttpHeaderToken = false;
                        startRequestBodyOrTitleToken = true;
                        return;
                    }

                    line = line.trim();
                    info.getHeaders()
                            .add(line);
                }
                // 解析HttpBody
                else if (startRequestBodyOrTitleToken) {
                    if (StringUtils.isBlank(line)) {
                        // 空行说明读到RequestBody结尾了
                        info.setRequestBody(StringUtils.join(requestBodys, "\n"));
                        requestBodys.clear();
                        startRequestBodyOrTitleToken = false;
                        startTitleToken = true;

                        ls.add(info);
                        // 重置
                        info = new HttpTxtInfo();
                    }
                    // Title
                    else if (line.trim()
                            .startsWith("###")) {
                        // 开始解析Title
                        startRequestBodyOrTitleToken = false;
                        startTitleToken = true;

                        ls.add(info);
                        // 重置
                        info = new HttpTxtInfo();

                        parseTitle(line);
                    }
                    // RequestBody
                    else {
                        requestBodys.add(line);
                    }
                } else {
                    throw new IllegalStateException("非法状态！");
                }
            }
        });
        if (!StringUtils.isBlank(info.getName())) {
            if (!CollectionUtils.isEmpty(requestBodys)) {
                info.setRequestBody(StringUtils.join(requestBodys, "\n"));
                requestBodys.clear();
            }
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
            startTitleToken = false;
            startEmbedParaOrRequestBodyToken = true;
        } else {
            // 非标头，跳过
            return;
        }
    }

    private void parseHttpInfo(String line) {
        String[] arr = StringUtils.split(line, " ");
        String method = arr[0].toUpperCase();
        if (methodSet.contains(method)) {
            info.setMethod(method);
            if (arr.length > 1) {
                info.setUrl(arr[1]);

                startEmbedParaOrRequestBodyToken = false;
                startHttpHeaderToken = true;
            } else {
                // 格式不正确
                throw new IllegalStateException("格式不正确，请求信息的Url不能包含空格");
            }
        } else {
            // 格式不正确
            throw new IllegalStateException("格式不正确，表头后必须是内嵌参数或请求信息");
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
        private Map<String, String> embedParaMap = new HashMap<>();
        /**
         * 请求头
         */
        private List<String> headers = new ArrayList<>();
        /**
         * 请求体
         */
        private String requestBody;

        public void check() {
            if (StringUtils.isBlank(method) || StringUtils.isBlank(url)) {
                throw new IllegalArgumentException("Http信息不能为空，格式：GET http://127.0.0.1:8080/getData");
            }

        }
    }

}
