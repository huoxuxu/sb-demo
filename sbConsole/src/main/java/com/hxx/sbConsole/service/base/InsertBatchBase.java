package com.hxx.sbConsole.service.base;

import com.hxx.sbConsole.model.enums.DBTypeEnum;
import com.hxx.sbConsole.service.face.IInsertBatch;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-02-28 17:28:06
 **/
public abstract class InsertBatchBase implements IInsertBatch {
    private final DBTypeEnum dbType;

    public InsertBatchBase(DBTypeEnum dbType) {
        this.dbType = dbType;
    }

    @Override
    public List<String> proc(List<String> titles, List<Object[]> data) {
        // 不同的DB，批量导入的SQL语法不对
        return null;
    }
}
