package com.hxx.sbcommon.common.office;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 10:47:40
 **/
@Slf4j
public class ExcelWriteHelper implements AutoCloseable {

    private boolean is97_03Excel;
    private File excel;
    Workbook workbook;
    Sheet sheet;
    private int rowIndex = 0;

    public ExcelWriteHelper(File newExcel, String sheetName) {
        this.excel = newExcel;

        String fileName = newExcel.getName().toLowerCase();
        //创建workbook
        this.is97_03Excel = fileName.endsWith(".xls");
        if (!this.is97_03Excel) {
            if (!fileName.endsWith(".xlsx")) throw new IllegalArgumentException("excel只支持后缀为.xls和.xlsx");

            this.is97_03Excel = false;
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }

        Sheet sheet = this.workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = this.workbook.createSheet(sheetName);
        }
        this.sheet = sheet;
    }

    @Override
    public void close() {
        try {
            if (this.workbook != null) {
                this.workbook.close();
            }
        } catch (Exception ignored) {

        }
    }

    public Workbook getWorkbook() {
        return this.workbook;
    }

    public Sheet getSheet(int sheetIndex) {
        return this.workbook.getSheetAt(sheetIndex);
    }

    public Sheet getSheet(String sheetName) {
        return this.workbook.getSheet(sheetName);
    }

    // 写入

    // 创建工作簿
    public Sheet createSheet(String sheetName) {
        return this.workbook.createSheet(sheetName);
    }

    /**
     * 合并单元格
     *
     * @param cellRangeAddress
     */
    public void mergeCell(CellRangeAddress cellRangeAddress) {
        // 合并单元格：参数1：行号 参数2：起始列号 参数3：行号 参数4：终止列号
        // new CellRangeAddress(0, 0, 0, 4)
        sheet.addMergedRegion(cellRangeAddress);
    }

    /**
     * 合并单元格
     *
     * @param blockInfo
     */
    public void mergeCell(BlockInfo blockInfo) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(blockInfo.getStartRowIndex(), blockInfo.getEndRowIndex(), blockInfo.getStartCellIndex(), blockInfo.getEndCellIndex());
        // 合并
        sheet.addMergedRegion(cellRangeAddress);
    }

    /**
     * 合并单元格
     *
     * @param blockInfos
     */
    public void mergeCell(List<BlockInfo> blockInfos) {
        if (CollectionUtils.isEmpty(blockInfos)) return;

        for (BlockInfo item : blockInfos) {
            mergeCell(item);
        }
    }

    /**
     * 保存到文件
     *
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        // 5.生成一张表。03版本的工作簿是以.xls结尾
        try (FileOutputStream fileOutputStream = new FileOutputStream(excel)) {
            // 输出
            workbook.write(fileOutputStream);
        }
    }

    /**
     * 数据写入工作簿行
     *
     * @param data
     */
    public void writeRow(List<String> data) {
        Row row = sheet.createRow(rowIndex);
        rowIndex++;
        for (int i = 0; i < data.size(); i++) {
            String item = data.get(i);
            Cell cell = row.createCell(i);
            cell.setCellValue(item);
        }
    }

    /**
     * 写入标题
     *
     * @param titles
     */
    public void writeTitle(List<String> titles) {
        Row row = sheet.createRow(rowIndex);
        rowIndex++;
        for (int i = 0; i < titles.size(); i++) {
            String item = titles.get(i);
            Cell cell = row.createCell(i);
            setCellStyle(cell, 30 * (Math.min(item.length(), 3) * 28));
            cell.setCellValue(item);
        }
    }

    /**
     * 写入数据, 注意：写入完成后会保存到文件中
     *
     * @param fieldMap 字段映射，key= 字段所在Excel列的索引 val=字段对应T 的属性名
     * @param data
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public <T> void writeDataWithModel(Map<Integer, String> fieldMap, List<T> data) throws InvocationTargetException, IllegalAccessException, IOException {
        if (CollectionUtils.isEmpty(data)) return;

        PropertyDescriptor[] props = null;
        for (T item : data) {
            if (props == null) props = BeanUtils.getPropertyDescriptors(item.getClass());

            Map<String, Object> valMap = new HashMap<>();
            for (PropertyDescriptor prop : props) {
                //得到属性的name
                String key = prop.getName();
                if (!fieldMap.containsValue(key) || key == "class") {
                    continue;
                }
                Method getMethod = prop.getReadMethod();
                if (getMethod == null) {
                    continue;
                }

                //执行get方法得到属性的值
                Object value = getMethod.invoke(item);
                valMap.put(key, value);
            }

            List<String> vals = new ArrayList<>();
            for (int i = 0; i < fieldMap.size(); i++) {
                String field = fieldMap.get(i);
                Object val = valMap.getOrDefault(field, null);
                if (val == null) {
                    val = "";
                }
                vals.add(val + "");
            }

            // 写入Excel
            writeRow(vals);
        }
    }

    /**
     * 导入Excel，注意：写入完成后会保存到文件中
     *
     * @param fieldMap 字段映射，key= 字段所在Excel列的索引 val=字段对应T 的属性名
     * @param data
     * @throws IOException
     */
    public void writeData(Map<Integer, String> fieldMap, List<Map<String, Object>> data) throws IOException {
        if (CollectionUtils.isEmpty(data)) return;

        for (Map<String, Object> valMap : data) {
            List<String> vals = new ArrayList<>();
            for (int i = 0; i < fieldMap.size(); i++) {
                String field = fieldMap.get(i);
                Object val = valMap.getOrDefault(field, null);
                if (val == null) {
                    val = "";
                }
                vals.add(val + "");
            }

            // 写入Excel
            writeRow(vals);
        }
    }

    public void setCellStyle(Cell cell, int cellWeight) {
        CellStyle cellStyle = workbook.createCellStyle();
        // 垂直居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        //文本内容自动换行
        cellStyle.setWrapText(true);
        // 设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 12);
        // 加粗
        font.setBold(true);
//        // 斜体
//        font.setItalic(false);
//        // 删除线
//        font.setStrikeout(false);
        cellStyle.setFont(font);
        // 设置背景色
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置边框（一般标题不设置边框，是标题下的所有表格设置边框）
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框

        //将新的样式赋给单元格
        cell.setCellStyle(cellStyle);

        Row row = cell.getRow();
        CellAddress address = cell.getAddress();
        //设置单元格的高度
        row.setHeight((short) (30 * 10));
        //设置单元格的宽度
//        sheet.setColumnWidth(address.getColumn(), 30 * 166);
        sheet.setColumnWidth(address.getColumn(), cellWeight);
    }

    /**
     * 合并单元格信息
     */
    @lombok.Data
    public static class BlockInfo {
        // 块起始行
        private int startRowIndex;
        // 块结束行
        private int endRowIndex;
        // 块起始列
        private int startCellIndex;
        // 块结束列
        private int endCellIndex;
        // 数据
        private Object cellVal;

        public BlockInfo() {
        }

        public BlockInfo(int startRowIndex, int endRowIndex, int startCellIndex, int endCellIndex, Object cellVal) {
            this.startRowIndex = startRowIndex;
            this.endRowIndex = endRowIndex;
            this.startCellIndex = startCellIndex;
            this.endCellIndex = endCellIndex;

            this.cellVal = cellVal;
        }

    }
}
