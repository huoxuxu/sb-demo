package com.hxx.sbcommon.common.office.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    public EasyExcelWriteHelper(String excelPath) throws IOException {
        this.excelOutputStream = Files.newOutputStream(Paths.get(excelPath));
        this.excelWriter = EasyExcel.write(this.excelOutputStream).build();
    }

    public EasyExcelWriteHelper(OutputStream excelOutputStream) {
        this.excelWriter = EasyExcel.write(excelOutputStream).build();
    }


    /**
     * 创建工作簿
     *
     * @param sheetIndex
     * @param sheetName
     * @param voCls
     * @return
     */
    public static WriteSheet createSheet(int sheetIndex, String sheetName, Class<?> voCls) {
        // 表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 单元格样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 初始化表格样式
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        // 开始写入工作簿
        return EasyExcel.writerSheet(sheetIndex, sheetName)
                .head(voCls)
                .registerWriteHandler(horizontalCellStyleStrategy)
                .build();
    }

    /**
     * 数据写入工作簿，可多次写入
     *
     * @param sheet
     * @param sheetData
     * @param <T>
     */
    public <T> void writeSheet(WriteSheet sheet, List<T> sheetData) {
        excelWriter = excelWriter.write(sheetData, sheet);
    }

    /**
     * 写入工作簿，创建工作簿并写入，适合一次写入工作簿
     *
     * @param sheetIndex
     * @param sheetName
     * @param sheetData
     * @param voCls
     * @param <T>
     */
    public <T> void writeSheet(int sheetIndex, String sheetName, List<T> sheetData, Class voCls) {
        WriteSheet sheet = createSheet(sheetIndex, sheetName, voCls);
        writeSheet(sheet, sheetData);
    }

    @Override
    public void close() throws Exception {
        if (this.excelWriter != null) excelWriter.finish();
        if (this.excelOutputStream != null) this.excelOutputStream.close();
    }
}
