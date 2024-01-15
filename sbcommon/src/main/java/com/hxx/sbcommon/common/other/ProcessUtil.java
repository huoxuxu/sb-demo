package com.hxx.sbcommon.common.other;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-22 9:20:22
 **/
@Slf4j
public class ProcessUtil {
    static Runtime CurrentRuntime;

    static {
        CurrentRuntime = Runtime.getRuntime();
    }

    /**
     * 执行命令
     *
     * @param cmdTxt sh /usr/updateSysTime.sh
     * @return
     * @throws IOException
     */
    public static Process exec(String cmdTxt) throws IOException {
        return CurrentRuntime.exec(cmdTxt);
    }

    /**
     * 执行命令，返回退出码
     *
     * @param cmdTxt sh /usr/updateSysTime.sh
     * @return 返回退出码
     * @throws IOException
     */
    public static int runCmdTxt(String cmdTxt) throws IOException {
        Process process = exec(cmdTxt);
        return process.exitValue();
    }

}
