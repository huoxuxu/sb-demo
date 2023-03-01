package com.hxx.sbConsole.service.insertBatch;

import com.hxx.sbConsole.model.enums.DBTypeEnum;
import com.hxx.sbConsole.service.fileParse.face.IParseFile;
import com.hxx.sbConsole.service.fileParse.impl.CsvParseFile;
import com.hxx.sbConsole.service.insertBatch.face.IInsertBatch;
import com.hxx.sbConsole.service.insertBatch.impl.MySQLInsertBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-01 17:44:50
 **/
public class InsertBatchServiceDemo {
    public static void main(String[] args) {
        try {
            // 数据来源
            IParseFile parseFile = new CsvParseFile();

            List<String> titles = new ArrayList<>();
            List<Object[]> data = new ArrayList<>();
            parseFile.read((num, row) -> {
                System.out.println("开始处理第 " + num + " 行");

                return true;
            });

            InsertBatchFactory fac = new InsertBatchFactory(DBTypeEnum.MySQL, "demo");
            IInsertBatch insertBatch = fac.getInsertBatch();
            List<String> sqls = insertBatch.proc(titles, data);
            for (String sql : sqls) {
                System.out.println(sql);
            }

        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok!");
    }
}
