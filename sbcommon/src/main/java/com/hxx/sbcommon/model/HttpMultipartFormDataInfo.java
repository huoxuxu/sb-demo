package com.hxx.sbcommon.model;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-21 13:30:48
 **/
@Data
public class HttpMultipartFormDataInfo implements Serializable {
    private static final long serialVersionUID = 2478003448230089914L;

    private String formName;
    private String formVal;
    private String contentType;
    private String fileName;

    private Map<String, String> headers;
    /**
     * 文件流
     */
    private InputStream inputStream;
}
