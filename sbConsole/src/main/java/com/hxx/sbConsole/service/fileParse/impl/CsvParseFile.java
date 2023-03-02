package com.hxx.sbConsole.service.fileParse.impl;

import com.hxx.sbConsole.service.fileParse.face.IParseFile;

import java.util.function.BiFunction;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-01 17:48:50
 **/
public class CsvParseFile implements IParseFile {
    /**
     * 读取行
     *
     * @param rowFunc
     */
    @Override
    public void read(BiFunction<Integer, Object[], Boolean> rowFunc) {
        for (int i = 0; i < 8; i++) {
            Object[] arr = {i, "A" + i};
            rowFunc.apply(i + 1, arr);
        }
    }
}
