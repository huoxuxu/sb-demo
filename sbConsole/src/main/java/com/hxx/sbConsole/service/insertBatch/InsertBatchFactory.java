package com.hxx.sbConsole.service.insertBatch;

import com.hxx.sbConsole.model.enums.DBTypeEnum;
import com.hxx.sbConsole.service.insertBatch.face.IInsertBatch;
import com.hxx.sbConsole.service.insertBatch.impl.MySQLInsertBatch;
import com.hxx.sbConsole.service.insertBatch.impl.SQLiteInsertBatch;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-28 17:28:06
 **/
public class InsertBatchFactory {
    // 目标类型
    private final DBTypeEnum dbType;
    // 表名
    private String tableName;

    public InsertBatchFactory(DBTypeEnum dbType, String tableName) {
        this.dbType = dbType;
        this.tableName = tableName;
    }


    /**
     * 获取IInsertBatch
     *
     * @return
     */
    public IInsertBatch getInsertBatch() {
        switch (this.dbType) {
            case MySQL:
                return new MySQLInsertBatch(this.tableName);
            case SQLite:
                return new SQLiteInsertBatch(this.tableName);
            case OracleDB:
            case UNKNOWN:
            default:
                return null;
        }
    }
}
