package com.hxx.sbcommon.module.spi.demo;

import com.hxx.sbcommon.module.spi.SpiServiceLoaderHelper;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-25 15:45:24
 **/
public class SpiServiceLoaderClient {
    public static void main(String[] args) {
        ProductPackageRemoteServiceInterface productPackageRemoteServiceInterface = SpiServiceLoaderHelper.getProductPackageRemoteServiceInterface();


    }
}
