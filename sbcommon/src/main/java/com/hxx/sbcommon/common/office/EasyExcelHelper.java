package com.hxx.sbcommon.common.office;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ReadListener;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-24 8:31:50
 **/
public class EasyExcelHelper {

    private File excel;

    private FileInputStream excelStream;

    public EasyExcelHelper(File _excel) {
        excel = _excel;
    }

    public EasyExcelHelper(FileInputStream _excelStream) {
        excelStream = _excelStream;
    }


    public void readToMap(int sheetIndex, ReadListener<Map<Integer, Object>> mapReadListener) {
        ExcelReaderBuilder readBuld = getExcelReaderBuilder(mapReadListener);
        readBuld.sheet(sheetIndex + 1).doRead();
    }

    public void readToMap(String sheetName, ReadListener<Map<Integer, Object>> mapReadListener) {
        ExcelReaderBuilder readBuld = getExcelReaderBuilder(mapReadListener);
        readBuld.sheet(sheetName).doRead();
    }

    public <TEasyExcelVO> void readEasyExcelVO(int sheetIndex, ReadListener<TEasyExcelVO> modelReadListener) {
        ExcelReaderBuilder readBuld = getExcelReaderBuilder2(modelReadListener);
        readBuld.sheet(sheetIndex + 1).doRead();
    }

    public <TEasyExcelVO> void readEasyExcelVO(String sheetName, ReadListener<TEasyExcelVO> modelReadListener) {
        ExcelReaderBuilder readBuld = getExcelReaderBuilder2(modelReadListener);
        readBuld.sheet(sheetName).doRead();
    }


    private <TEasyExcelVO> ExcelReaderBuilder getExcelReaderBuilder2(ReadListener<TEasyExcelVO> modelReadListener) {
        ExcelReaderBuilder readBuld;
        if (excel != null) {
            readBuld = EasyExcel.read(excel, modelReadListener);
        } else {
            readBuld = EasyExcel.read(excelStream, modelReadListener);
        }
        return readBuld;
    }


    private ExcelReaderBuilder getExcelReaderBuilder(ReadListener<Map<Integer, Object>> mapReadListener) {
        ExcelReaderBuilder readBuld;
        if (excel != null) {
            readBuld = EasyExcel.read(excel, mapReadListener);
        } else {
            readBuld = EasyExcel.read(excelStream, mapReadListener);
        }
        return readBuld;
    }

    /**
     * TEasyExcelVO示例
     */
    @lombok.Data
    @lombok.ToString
    public class DemoEasyExcelVO {
        @ExcelProperty(value = "编码")
        private String code;

        @ExcelProperty(value = "名称")
        private String name;
    }

    /**
     * 解析示例
     */
    public class DemoAnalysisEventListener extends AnalysisEventListener<Map<Integer, Object>> {

        private int startRowIndex = 1;
        private int startCellIndex = 0;

        private int curRowIndex = 0;

        //用于存储表头的信息
        private Map<Integer, String> headMap;

        private List<Map<Integer, Object>> data = new ArrayList<>();

        /**
         * 数据默认从第2行，第1列开始解析
         */
        public DemoAnalysisEventListener() {
        }

        /**
         * 数据默认从指定行，指定列开始解析
         *
         * @param startRowIndex  数据起始行索引
         * @param startCellIndex 数据起始列索引
         */
        public DemoAnalysisEventListener(int startRowIndex, int startCellIndex) {
            this.startRowIndex = startRowIndex;
            this.startCellIndex = startCellIndex;
        }

        /**
         * 获取读取到的数据
         *
         * @return
         */
        public List<Map<Integer, Object>> getData() {
            return data;
        }

        // 在读取完一行数据后调用
        @Override
        public void invoke(Map<Integer, Object> stringObjectMap, AnalysisContext analysisContext) {
            if (curRowIndex >= startRowIndex) {
                Map<Integer, Object> map = new HashMap<>();
                for (int i = startCellIndex; i < stringObjectMap.size(); i++) {
                    Object o = stringObjectMap.get(i);
                    map.put(i, o);
                }
                data.add(map);
            }

            curRowIndex++;
        }


        //读取excel表头信息
        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            this.headMap = headMap;
            curRowIndex++;
        }

        // 在读取完所有数据后调用
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            System.out.println("表格读取完成！");
        }
    }
}
