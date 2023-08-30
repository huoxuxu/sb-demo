package com.hxx.sbcommon.common.office.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * EasyExcel 写入工作簿帮助类
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-30 15:43:14
 **/
public class EasyExcelWriteHelper implements AutoCloseable {
    private OutputStream excelOutputStream;
    private ExcelWriter excelWriter;

    public EasyExcelWriteHelper(String excelPath) throws FileNotFoundException {
        this.excelOutputStream = new FileOutputStream(excelPath);
        this.excelWriter = EasyExcel.write(this.excelOutputStream).build();
    }

    public EasyExcelWriteHelper(OutputStream excelOutputStream) {
        this.excelWriter = EasyExcel.write(excelOutputStream).build();
    }

    /**
     * 写入工作簿
     *
     * @param sheetIndex
     * @param sheetName
     * @param sheetData
     * @param voCls
     * @param <T>
     */
    public <T> void writeSheet(int sheetIndex, String sheetName, List<T> sheetData, Class voCls) {
        // 表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 单元格样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 开始写入工作簿
        WriteSheet sheet = EasyExcel.writerSheet(sheetIndex, sheetName)
                .head(voCls)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .build();
        excelWriter = excelWriter.write(sheetData, sheet);
    }

    @Override
    public void close() throws Exception {
        if (this.excelWriter != null) excelWriter.finish();
        if (this.excelOutputStream != null) this.excelOutputStream.close();
    }
}
