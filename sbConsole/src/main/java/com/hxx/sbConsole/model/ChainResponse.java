package com.hxx.sbConsole.model;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-07 14:23:03
 **/
@Data
public class ChainResponse {
    private String errCode;
    private String errMsg;

    private String userName;
    private int age;
}
