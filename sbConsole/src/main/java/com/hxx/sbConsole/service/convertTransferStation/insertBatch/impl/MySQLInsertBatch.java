package com.hxx.sbConsole.service.convertTransferStation.insertBatch.impl;

import com.hxx.sbConsole.service.convertTransferStation.insertBatch.face.IInsertBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-01 17:38:44
 **/
public class MySQLInsertBatch implements IInsertBatch {
    // 表名
    private String tableName;

    public MySQLInsertBatch(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 处理数据获取批量新增SQL
     *
     * @param titles
     * @param data
     * @return
     */
    @Override
    public List<String> proc(List<String> titles, List<Object[]> data) {
        // 生成MySQL支持的批量新增的SQL
        List<String> ls = new ArrayList<>();
        ls.add("INSERT INTO XXXX " + data.size());
        return ls;
    }

}
