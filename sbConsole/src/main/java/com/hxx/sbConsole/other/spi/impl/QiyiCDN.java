package com.hxx.sbConsole.other.spi.impl;

import com.hxx.sbConsole.other.spi.UploadCDN;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-29 13:09:35
 **/
public class QiyiCDN implements UploadCDN {
    @Override
    public void upload(String url) {
        System.out.println("qiyi cdn: " + url);
    }
}
