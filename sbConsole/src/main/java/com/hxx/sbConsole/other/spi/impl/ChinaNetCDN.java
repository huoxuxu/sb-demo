package com.hxx.sbConsole.other.spi.impl;

import com.hxx.sbConsole.other.spi.UploadCDN;

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
