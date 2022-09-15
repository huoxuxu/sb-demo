package com.hxx.sbcommon.common.office;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-09 10:09:20
 **/
@Slf4j
public class ExcelHelper implements Closeable {
    private final String fileName;
    private final InputStream inputStream;

    private boolean is97_03Excel;

    private Excel97_03Helper excel97_03Helper;

    private final Workbook workbook;
    private final int sheetCount;

    public ExcelHelper(String fileName, InputStream inputStream) throws IOException {
        this.fileName = fileName;
        this.inputStream = inputStream;
        OftenUtil.assertCond(StringUtils.isBlank(this.fileName), "excel名称不能为空");
        OftenUtil.assertCond(this.inputStream == null, "excel流不能为空");

        this.is97_03Excel = fileName.endsWith(".xls");
        if (!this.is97_03Excel) {
            OftenUtil.assertCond(!fileName.endsWith(".xlsx"), "excel只支持后缀为.xls和.xlsx");

            this.is97_03Excel = true;
        }

        if (this.is97_03Excel) {
            this.excel97_03Helper = new Excel97_03Helper(fileName, inputStream);
            this.workbook = this.excel97_03Helper.getWorkbook();
        } else {
            this.workbook = new XSSFWorkbook(this.inputStream);
        }
        this.sheetCount = this.workbook.getNumberOfSheets();

        OftenUtil.assertCond(this.sheetCount < 1, "未找到工作簿");

    }

    @Override
    public void close() throws IOException {
        if (this.is97_03Excel) {
            // 97-03
            if (this.excel97_03Helper != null) {
                this.excel97_03Helper.close();
            }
        } else {
            if (this.workbook != null) {
                this.workbook.close();
            }
        }
    }

    /**
     * 解析Excel数据
     *
     * @param sheetIndex
     * @param startRowIndex
     * @param colCount
     * @return
     */
    public List<RowItem> parseRows(int sheetIndex, int startRowIndex, int colCount) {
        OftenUtil.assertCond(sheetCount <= sheetIndex, "不包含工作簿，索引：" + sheetIndex);
        Sheet sheet = this.workbook.getSheetAt(sheetIndex);
        OftenUtil.assertCond(sheet == null, "未正常解析工作簿，索引：" + sheetIndex);

        List<RowItem> ls = new ArrayList<>();
        // 得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();

        for (int r = startRowIndex; r < totalRows; r++) {
            Row row = sheet.getRow(r);
            // 实际包含数据的行的个数，如果数据在Z1 则行数也是1
            // int physicalNumberOfCells = row.getPhysicalNumberOfCells();
            // 获取最后单元格的列号
            // row.getLastCellNum()
            // 行号
            int rowNum = r + 1;

            if (isEmpty(row)) {
                continue;
            }

            List<String> data = new ArrayList<>();
            for (int i = 0; i < colCount; i++) {
                Cell cell = row.getCell(i);
                String cellValue = getCellValue(cell);
                data.add(cellValue);
            }
            RowItem rowItem = new RowItem(rowNum, data);
            ls.add(rowItem);
        }

        return ls;
    }

//    // 获取工作簿
//    private Sheet getSheet(int sheetIndex) throws Exception {
//        // 通过流解析excel
//        if (fileName.endsWith(".xls")) {
//            try (POIFSFileSystem poifsFileSystem = new POIFSFileSystem(this.inputStream)) {
//                try (HSSFWorkbook book = new HSSFWorkbook(poifsFileSystem)) {
//                    int numberOfSheets = book.getNumberOfSheets();
//                    if (numberOfSheets < 1 || numberOfSheets <= sheetIndex) {
//                        return null;
//                    }
//
//                    return book.getSheetAt(sheetIndex);
//                }
//            }
//        } else if (fileName.endsWith(".xlsx")) {
//            try (XSSFWorkbook book = new XSSFWorkbook(this.inputStream)) {
//                int numberOfSheets = book.getNumberOfSheets();
//                if (numberOfSheets < 1 || numberOfSheets <= sheetIndex) {
//                    return null;
//                }
//
//                return book.getSheetAt(sheetIndex);
//            }
//        } else {
//            return null;
//        }
//    }

    /**
     * 获取Excel单元格的值
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING: {
                RichTextString cellValue = cell.getRichStringCellValue();
                if (cellValue != null) {
                    String valStr = cellValue.getString();
                    if (!StringUtils.isBlank(valStr)) {
                        return valStr.trim();
                    }
                } else {
                    return ((XSSFCell) cell).getRawValue();
                }

                return "";
            }
            case NUMERIC: {
                if (cell instanceof XSSFCell) {
                    return ((XSSFCell) cell).getRawValue();
                } else {
                    return cell.getNumericCellValue() + "";
                }
//                boolean isDate = org.apache.poi.hssf.usermodel.HSSFDateUtil.isCellDateFormatted(cell);
//                if (isDate) {
//                    Date dateCellValue = cell.getDateCellValue();
//                    if (dateCellValue == null) {
//                        return "";
//                    }
//
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    return format.format(dateCellValue);
//                } else {
//                    return ((XSSFCell) cell).getRawValue();
//                }
            }
            case BOOLEAN: {
                return (cell.getBooleanCellValue() + "").toLowerCase();
            }
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    private static boolean isEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (!isEmpty(cell)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isEmpty(Cell cell) {
        return cell == null || cell.getCellType() == CellType.BLANK;
    }

    @Data
    public static class RowItem {
        private int rowNum;
        private List<String> itemArray = new ArrayList<>();

        public RowItem() {
        }

        public RowItem(int rowNum, List<String> itemArray) {
            this.rowNum = rowNum;
            if (!CollectionUtils.isEmpty(itemArray)) {
                this.itemArray = itemArray;
            }
        }

    }

    // 处理97-03的excel
    static class Excel97_03Helper implements Closeable {
        private final String fileName;
        private final InputStream inputStream;

        private final POIFSFileSystem poifsFileSystem;
        private final Workbook workbook;

        /**
         * @param fileName
         * @param inputStream
         */
        public Excel97_03Helper(String fileName, InputStream inputStream) throws IOException {
            this.fileName = fileName;
            this.inputStream = inputStream;

            OftenUtil.assertCond(StringUtils.isBlank(this.fileName), "excel名称不能为空");
            OftenUtil.assertCond(this.inputStream == null, "excel流不能为空");

            this.poifsFileSystem = new POIFSFileSystem(this.inputStream);
            this.workbook = new HSSFWorkbook(poifsFileSystem);
        }


        @Override
        public void close() throws IOException {
            if (this.workbook != null) {
                this.workbook
                        .close();
            }
            if (this.poifsFileSystem != null) {
                this.poifsFileSystem.close();
            }

        }

        public Workbook getWorkbook() {
            return workbook;
        }
    }
}
