package com.hxx.sbcommon.common.office.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ICell;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 10:47:40
 **/
@Slf4j
public class POIExcelWriteUseful implements AutoCloseable {

    private boolean is97_03Excel;
    private File excelFile;
    Workbook workbook;
    Sheet sheet;
    private int rowIndex = 0;

    /**
     * @param newExcel  excel保存的地址 如果文件存在，会直接覆盖
     * @param sheetName excel数据保存的工作簿名
     */
    public POIExcelWriteUseful(File newExcel, String sheetName) {
        this.excelFile = newExcel;

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

    // =========================写入=========================

    /**
     * 创建空Excel文件
     *
     * @param excel     待新建的Excel文件,存在覆盖,不存在新增
     * @param sheetName 工作簿名
     */
    public static void createEmptyExcel(File excel, String sheetName) throws IOException {
        try (POIExcelWriteUseful writeUseful = new POIExcelWriteUseful(excel, sheetName)) {
            // 保存到Excel文件中
            writeUseful.saveToFile();
        }
    }

    /**
     * 创建工作簿
     *
     * @param sheetName
     */
    public Sheet createSheet(String sheetName) {
        return this.workbook.createSheet(sheetName);
    }

    /**
     * 保存到文件
     *
     * @throws IOException
     */
    public void saveToFile() throws IOException {
        // 5.生成一张表。03版本的工作簿是以.xls结尾
        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFile)) {
            // 输出
            workbook.write(fileOutputStream);
        }
    }

    /**
     * 数据写入当前工作簿的行，不支持的类型会抛出异常
     *
     * @param data
     * @param procCellStyle 处理单元格样式,p1=单元格索引，p2=单元格
     */
    public void writeExcelRow(List<Object> data, BiConsumer<Integer, Cell> procCellStyle) {
        Row row = sheet.createRow(rowIndex);
        rowIndex++;
        for (int i = 0; i < data.size(); i++) {
            Cell cell = row.createCell(i);
            if (procCellStyle != null) procCellStyle.accept(i, cell);

            Object val = data.get(i);
            if (val == null) {
                cell.setCellType(CellType.BLANK);
                continue;
            }
            // 写入单元格
            ExcelCellTools.write2Cell(val, cell);
        }
    }

    /**
     * 写入标题
     *
     * @param titles
     */
    public void writeTitle(List<String> titles) {
        List<Object> ls = titles.stream().map(d -> (Object) d).collect(Collectors.toList());
        writeExcelRow(ls, (cellInd, cell) -> {
            String item = titles.get(cellInd);
            setCellStyle(cell, 30 * (Math.min(item.length(), 3) * 28));
        });
    }

    /**
     * 导入Excel，注意：写入完成后会保存到文件中
     *
     * @param fieldMap 字段映射，key= 字段所在Excel列的索引 val=字段对应T 的属性名
     * @param data
     * @throws IOException
     */
    public void writeData(Map<Integer, String> fieldMap, List<Map<String, Object>> data) throws IOException {
        for (Map<String, Object> valMap : data) {
            List<Object> vals = new ArrayList<>();
            for (int i = 0; i < fieldMap.size(); i++) {
                String field = fieldMap.get(i);
                Object val = valMap.getOrDefault(field, null);

                vals.add(val);
            }
            // 写入Excel
            writeExcelRow(vals, null);
        }
    }

    /**
     * 设置日期格式--使用Excel内嵌的格式
     *
     * @param cell
     * @param val
     * @param fmt  m/d/yy h:mm
     */
    public void writeCellVal(HSSFCell cell, Date val, String fmt) {
        cell.setCellValue(val);
        HSSFCellStyle style = ((HSSFWorkbook) workbook).createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat(fmt));
        cell.setCellStyle(style);
    }

    /**
     * 设置保留2位小数--使用Excel内嵌的格式
     *
     * @param cell
     * @param val
     * @param fmt  0.00
     */
    public void writeCellVal(HSSFCell cell, Double val, String fmt) {
        cell.setCellValue(val);
        HSSFCellStyle style = ((HSSFWorkbook) workbook).createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat(fmt));
        cell.setCellStyle(style);
    }

    /**
     * 设置货币格式--使用自定义的格式
     *
     * @param cell
     * @param val
     * @param fmt  货币格式:￥#,##0 百分比:0.00% 中文大写:[DbNum2][$-804]0 科学计数法:0.00E+00
     */
    public void writeCellMoneyVal(HSSFCell cell, Double val, String fmt) {
        cell.setCellValue(val);
        HSSFCellStyle style = ((HSSFWorkbook) workbook).createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat(fmt));
        cell.setCellStyle(style);
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

    // 处理Excel的Cell工具类
    static class ExcelCellTools {

        /**
         * java值写入Cell，不支持的java类型会报错
         *
         * @param val
         * @param cell
         */
        private static void write2Cell(Object val, Cell cell) {
            // 数值型double 日期型Date 文本String 布尔型boolean 空null
            {
                Double cval = ExcelCellTools.getNumericVal(val);
                if (cval != null) {
                    cell.setCellValue(cval);
                    return;
                }
            }
            {
                String cval = ExcelCellTools.getStringVal(val);
                if (cval != null) {
                    cell.setCellValue(cval);
                    return;
                }
            }
            {
                Boolean cval = ExcelCellTools.getBooleanVal(val);
                if (cval != null) {
                    cell.setCellValue(cval);
                    return;
                }
            }
            {
                Date cval = ExcelCellTools.getDateVal(val);
                if (cval != null) {
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    cell.setCellValue(f.format(cval));
                    return;
                }
            }

            throw new IllegalStateException("不支持的数据类型！" + val.getClass());
        }

        private static Double getNumericVal(Object val) {
            // 整型
            if (val instanceof Byte) {
                return ((Byte) val).doubleValue();
            }
            if (val instanceof Short) {
                return ((Short) val).doubleValue();
            }
            if (val instanceof Integer) {
                return ((Integer) val).doubleValue();
            }
            if (val instanceof Long) {
                return ((Long) val).doubleValue();
            }

            // 浮点型
            if (val instanceof Float) {
                return ((Float) val).doubleValue();
            }
            if (val instanceof Double) {
                return (Double) val;
            }
            if (val instanceof BigDecimal) {
                return ((BigDecimal) val).doubleValue();
            }
            return null;
        }

        private static Date getDateVal(Object val) {
            if (val instanceof Date) {
                return (Date) val;
            }

            if (val instanceof LocalDateTime) {
                return Date.from(((LocalDateTime) val).atZone(ZoneId.systemDefault())
                        .toInstant());
            }

            if (val instanceof LocalDate) {
                return Date.from(((LocalDate) val).atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant());
            }
            return null;
        }

        private static String getStringVal(Object val) {
            // 字符串
            if (val instanceof String) {
                return (String) val;
            }
            return null;
        }

        private static Boolean getBooleanVal(Object val) {
            // 布尔型
            if (val instanceof Boolean) {
                return (Boolean) val;
            }
            return null;
        }

    }
}
