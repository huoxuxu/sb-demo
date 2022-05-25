package com.hxx.sbcommon.common.http;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-05-25 17:09:14
 **/
@Data
public class JsonRequest {
    private String requestId;
    private String appId;
    private long timestamp;

    private String aseKey;

    private String body;
    private String sign;
}
