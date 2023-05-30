package com.hxx.sbcommon.common.office;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-26 13:49:30
 **/
@Slf4j
public class POIUtil {

    /**
     * 获取Workbook, 支持97-03和07以上的excel
     *
     * @param excel
     * @param readFlag 是否读取，读取时，excel必须存在，写入时，如果存在，则追加数据，如果不存在，则新增数据
     * @return
     * @throws IOException
     */
    public static Workbook getWorkbook(File excel, boolean readFlag) throws IOException {
        String fileName = excel.getName().toLowerCase();

        boolean is97_03Excel = fileName.endsWith(".xls");
        if (excel.exists()) {
            try (FileInputStream inputStream = FileUtils.openInputStream(excel)) {
                if (is97_03Excel) {
                    ExcelHelper.Excel97_03Helper excel97_03Helper = new ExcelHelper.Excel97_03Helper(fileName, inputStream);
                    return excel97_03Helper.getWorkbook();
                } else {
                    return new XSSFWorkbook(inputStream);
                }
            }
        } else {
            if (readFlag) throw new IllegalArgumentException("excel不存在！");

            if (is97_03Excel) {
                return new HSSFWorkbook();
            } else {
                if (!fileName.endsWith(".xlsx")) throw new IllegalArgumentException("excel只支持后缀为.xls和.xlsx");

                return new XSSFWorkbook();
            }
        }
    }

    /**
     * 保存Workbook 到文件
     *
     * @param workbook
     * @param targetExcel 保存到的excel文件
     * @throws IOException
     */
    public static void saveToFile(Workbook workbook, File targetExcel) throws IOException {
        // 5.生成一张表。03版本的工作簿是以.xls结尾
        try (FileOutputStream fileOutputStream = new FileOutputStream(targetExcel)) {
            // 输出
            workbook.write(fileOutputStream);
        }
    }

    /**
     * 数据写入工作簿行，会创建行
     *
     * @param row  准备写入的行
     * @param data
     */
    public static void writeRow(Row row, List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            Cell cell = row.createCell(i);
            cell.setCellValue(item);
        }
    }

    /**
     * 写入行，会创建行
     *
     * @param row
     * @param data         准备写入的行
     * @param cellConsumer
     */
    public static void writeRow(Row row, List<String> data, Consumer<Cell> cellConsumer) {
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            Cell cell = row.createCell(i);
            cellConsumer.accept(cell);
            cell.setCellValue(item);
        }
    }

}
