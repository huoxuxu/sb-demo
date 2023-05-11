package com.hxx.sbConsole.module.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.hxx.sbConsole.module.easyExcel.handler.ExcelFillCelMergeStrategy;
import com.hxx.sbConsole.module.easyExcel.handler.ExcelFillCellMergeStrategyUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-10 11:10:40
 **/
public class EasyExcelDemo {

    public static void main(String[] args) {
        try {
            String path = "d:/tmp/easyExcelDemo.xls";
            demo(path);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("ok");
    }

    public static void demo(String path) throws IOException {
        List<CityCapacityPo> data = getData();

        CityCapacityPo capacityPo = new CityCapacityPo();
        capacityPo.setExport("汇总");
        capacityPo.setTime("汇总");
        capacityPo.setDirection("汇总");
        //遍历列表，求各数据汇总
        capacityPo.setData1(data.stream()
                .filter(Po -> Po.getData1() != null)
                .mapToDouble(CityCapacityPo::getData1)
                .sum());
        capacityPo.setData2(data.stream()
                .filter(Po -> Po.getData2() != null)
                .mapToDouble(CityCapacityPo::getData2)
                .sum());
        data.add(capacityPo);

        List<CellLineRange> cellLineRanges = new ArrayList<>();
        //设置 某列 需要合并
        int[] mergeColumnIndex = {0, 1};
        //设置第几行开始合并
        int mergeRowIndex = 1;

        cellLineRanges.add(new CellLineRange(0, 2));

        File file = new File(path);
        if (file.exists()) FileUtils.delete(file);

        List<WriteHandler> handlers = new ArrayList<>();
        {
            // 自定义合并
            handlers.add(new ExcelFillCellMergeStrategyUtils(mergeRowIndex, mergeColumnIndex));
            // 汇总合并
            handlers.add(new ExcelFillCelMergeStrategy(cellLineRanges, data.size() - 1));
        }
        Class<?> easyExcelVoCls = CityCapacityPo.class;
        EasyExcelUtil.generateExcel(path, data, easyExcelVoCls, "data", handlers);
    }

    private static List<CityCapacityPo> getData() {
        List<CityCapacityPo> ls = new ArrayList<>();
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约2", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约3", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约4", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约5", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约6", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "美国", "纽约7", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "韩国", "汉城", 1024D, 2048D));
        ls.add(new CityCapacityPo("2023-05-01", "韩国", "汉城2", 1024D, 2048D));
        int num = 0;
        for (CityCapacityPo item : ls) {
            num++;
            item.data1 += num;
            item.data2 += num;
        }
        return ls;
    }


    @lombok.Data
    @AllArgsConstructor
    public static class CityCapacityPo {
        @ExcelProperty(value = "时间", index = 0)
        private String time;

        @ExcelProperty(value = "出口", index = 1)
        private String export;

        @ExcelProperty(value = "地市", index = 2)
        private String direction;

        @ExcelProperty(value = "数据1", index = 3)
        private Double data1;

        @ExcelProperty(value = "数据2", index = 4)
        private Double data2;

        public CityCapacityPo() {
        }
    }

    @lombok.Data
    @AllArgsConstructor
    public static class CellLineRange {
        /**
         * 起始列
         */
        private int firstCol;

        /**
         * 结束列
         */
        private int lastCol;
    }
}
