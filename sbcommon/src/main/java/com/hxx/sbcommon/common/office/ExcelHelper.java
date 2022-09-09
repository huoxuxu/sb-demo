package com.hxx.sbcommon.common.office;

import com.hxx.sbcommon.common.basic.OftenUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-09 10:09:20
 **/
@Slf4j
public class ExcelHelper {
    private String fileName;
    private InputStream inputStream;

    /**
     * @param fileName
     * @param inputStream
     */
    public ExcelHelper(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    /**
     * 解析Excel数据
     *
     * @param sheetIndex
     * @param startRowIndex
     * @param colCount
     * @return
     * @throws Exception
     */
    public List<RowItem> parseRows(int sheetIndex, int startRowIndex, int colCount) throws Exception {
        Sheet sheet = getSheet(sheetIndex);
        OftenUtil.assertCond(sheet == null, "未正常解析工作簿");

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

    /**
     * 获取工作簿
     *
     * @return
     * @throws Exception
     */
    private Sheet getSheet(int sheetIndex) throws Exception {
        // 通过流解析excel
        if (fileName.endsWith(".xls")) {
            try (POIFSFileSystem poifsFileSystem = new POIFSFileSystem(this.inputStream)) {
                try (HSSFWorkbook book = new HSSFWorkbook(poifsFileSystem)) {
                    int numberOfSheets = book.getNumberOfSheets();
                    if (numberOfSheets < 1 || numberOfSheets <= sheetIndex) {
                        return null;
                    }

                    return book.getSheetAt(sheetIndex);
                }
            }
        } else if (fileName.endsWith(".xlsx")) {
            try (XSSFWorkbook book = new XSSFWorkbook(this.inputStream)) {
                int numberOfSheets = book.getNumberOfSheets();
                if (numberOfSheets < 1 || numberOfSheets <= sheetIndex) {
                    return null;
                }

                return book.getSheetAt(sheetIndex);
            }
        } else {
            return null;
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

    @Data
    public static class RowItem {
        private int rowNum;
        private List<String> itemArray;

        public RowItem() {
        }

        public RowItem(int rowNum, List<String> itemArray) {
            this.rowNum = rowNum;
            this.itemArray = itemArray;
        }

    }
}
