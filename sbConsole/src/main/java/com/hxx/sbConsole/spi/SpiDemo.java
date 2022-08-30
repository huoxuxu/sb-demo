package com.hxx.sbConsole.spi;

import java.util.ServiceLoader;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-08-29 13:13:54
 **/
public class SpiDemo {
    public static void main(String[] args) {
        ServiceLoader<UploadCDN> uploadCDN = ServiceLoader.load(UploadCDN.class);
        for (UploadCDN u : uploadCDN) {
            u.upload("filePath");
        }
        /*
        输出：
        chinaNet cdn: filePath
        qiyi cdn: filePath
        */
    }
}
