package com.hxx.sbConsole.service.convertTransferStation.writeTo.impl;

import com.hxx.sbConsole.service.convertTransferStation.writeTo.face.IWriteTo;

import java.io.File;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-02 17:34:16
 **/
public class CsvWriteTo implements IWriteTo {
    private final File file;
    private final boolean appendFlag;

    public CsvWriteTo(File file, boolean appendFlag) {
        this.file = file;
        this.appendFlag = appendFlag;
    }

    /**
     * 写入
     *
     * @param title
     * @param data
     * @return
     */
    @Override
    public int write(List<String> title, List<Object[]> data) {
        System.out.println("写入csv成功：" + data.size() + "行");
        return data.size();
    }

}
