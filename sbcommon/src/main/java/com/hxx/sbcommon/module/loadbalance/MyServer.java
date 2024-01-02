package com.hxx.sbcommon.module.loadbalance;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 16:27:26
 **/
@Data
public class MyServer {

    private String ip;

    private int weight;
}
