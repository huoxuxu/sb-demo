package com.hxx.sbConsole.module.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-10 13:05:25
 **/
@Slf4j
public class EasyExcelUtil {

    /**
     * 生成Excel
     *
     * @param excelPath         后缀xlsx
     * @param data
     * @param easyExcelVoCls
     * @param <TEasyExcelVoCls>
     */
    public static <TEasyExcelVoCls> void generateExcel(String excelPath, List<TEasyExcelVoCls> data, Class<?> easyExcelVoCls) {
        generateExcel(excelPath, data, easyExcelVoCls, "data", new ArrayList<>());
    }

    /**
     * 生成Excel
     *
     * @param excelPath
     * @param data
     * @param easyExcelVoCls
     * @param sheetName
     * @param handlers
     * @param <TEasyExcelVoCls>
     */
    public static <TEasyExcelVoCls> void generateExcel(String excelPath, List<TEasyExcelVoCls> data, Class<?> easyExcelVoCls, String sheetName, List<WriteHandler> handlers) {
        ExcelWriterBuilder write = EasyExcel.write(excelPath, easyExcelVoCls);

        if (!CollectionUtils.isEmpty(handlers)) {
            for (WriteHandler handler : handlers) {
                write.registerWriteHandler(handler);
            }
        }
        write.sheet(sheetName)
                .doWrite(data);
    }

}
