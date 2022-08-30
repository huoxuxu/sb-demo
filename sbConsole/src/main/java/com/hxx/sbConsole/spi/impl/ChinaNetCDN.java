package com.hxx.sbConsole.spi.impl;

import com.hxx.sbConsole.spi.UploadCDN;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-29 13:10:08
 **/
public class ChinaNetCDN implements UploadCDN {
    @Override
    public void upload(String url) {
        System.out.println("chinaNet cdn: " + url);
    }
}
