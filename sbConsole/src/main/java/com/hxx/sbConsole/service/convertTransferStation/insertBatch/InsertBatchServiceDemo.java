package com.hxx.sbConsole.service.convertTransferStation.insertBatch;

import com.hxx.sbConsole.model.enums.DBTypeEnum;
import com.hxx.sbConsole.service.convertTransferStation.fileParse.face.IParseFile;
import com.hxx.sbConsole.service.convertTransferStation.fileParse.impl.CsvParseFile;
import com.hxx.sbConsole.service.convertTransferStation.insertBatch.face.IInsertBatch;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-01 17:44:50
 **/
public class InsertBatchServiceDemo {
    public static void main(String[] args) {
        try {
            int batchSize = 3;
            // 数据来源
            IParseFile parseFile = new CsvParseFile();
            // 目标数据 数据库、文件
            InsertBatchFactory fac = new InsertBatchFactory(DBTypeEnum.MySQL, "demo");
            IInsertBatch insertBatch = fac.getInsertBatch();

            List<String> titles = new ArrayList<>();
            List<Object[]> data = new ArrayList<>();
            parseFile.read((num, row) -> {
                System.out.println("开始处理第 " + num + " 行");
                if (num == 1) {
                    List<String> rowls = Arrays.stream(row)
                            .map(d -> d == null ? "" : d + "")
                            .collect(Collectors.toList());
                    titles.addAll(rowls);
                } else {
                    data.add(row);
                    if (data.size() == batchSize) {
                        // 通知可以处理了
                        consumer(insertBatch, titles, data);
                        data.clear();
                    }
                }
                return true;
            });

            if(!CollectionUtils.isEmpty(data)){
                // 通知可以处理了
                consumer(insertBatch, titles, data);
                data.clear();
            }

        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok!");
    }

    private static void consumer(IInsertBatch insertBatch, List<String> titles, List<Object[]> data) {
        List<String> sqls = insertBatch.proc(titles, data);
        for (String sql : sqls) {
            System.out.println("sql：" + sql);
        }
    }
}
