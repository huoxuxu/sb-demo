package com.hxx.sbConsole.spi.impl;

import com.hxx.sbConsole.spi.UploadCDN;

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
