package com.hxx.sbConsole.service.fileParse.face;

import java.util.function.BiFunction;

/**
 * 解析文件
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-01 17:46:02
 **/
public interface IParseFile {
    /**
     * 读取行
     *
     * @param rowFunc
     */
    void read(BiFunction<Integer, Object[], Boolean> rowFunc);

}
