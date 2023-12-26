package com.hxx.sbcommon.common.hardware;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-12-26 8:47:27
 **/
public class OSUtil {

    public boolean isWindows(){
        return "windows".equalsIgnoreCase(System.getProperty("os.name"));
    }

    public boolean isLinux(){
        return "linux".equalsIgnoreCase(System.getProperty("os.name"));
    }

}
