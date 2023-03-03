package com.hxx.sbConsole.service.convertTransferStation.writeTo.impl;

import com.hxx.sbConsole.service.convertTransferStation.writeTo.face.IWriteTo;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-02 17:32:48
 **/
public class MySQLWriteTo implements IWriteTo {
    private final Object conn;
    private final String tableName;

    public MySQLWriteTo(Object conn, String tableName) {
        this.conn = conn;
        this.tableName = tableName;
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
        System.out.println("写入mysql成功：" + data.size() + "行");
        return data.size();
    }

}
