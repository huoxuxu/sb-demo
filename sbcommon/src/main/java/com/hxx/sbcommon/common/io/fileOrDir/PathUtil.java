package com.hxx.sbcommon.common.io.fileOrDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-04 13:16:49
 **/
public class PathUtil {

    /**
     * 连接两个路径
     *
     * @param path1
     * @param path2
     * @return
     */
    public static String combine(String path1, String path2) {
        path1 = path1.trim()
                .replace("\\\\", "/");
        path2 = path2.trim()
                .replace("\\\\", "/");
        // 将\转换为/
        path1 = path1.replace('\\', '/');
        path2 = path2.replace('\\', '/');
        // 兼容windows
        path1 = path1.replace("//", "/");
        path2 = path2.replace("//", "/");
        // path1最后为/
        if (!path1.endsWith("/")) {
            path1 = path1 + "/";
        }

        if (path2.startsWith("/")) {
            path2 = path2.substring(1);
        }
        return path1 + path2;
    }

    /**
     * 读取文件为文本
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readAllText(String filePath) throws IOException {
        File f = new File(filePath);
        return FileUtil.readAllText(f, StandardCharsets.UTF_8);
    }

}
