package com.hxx.sbweb.common;

import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * 
 * @author zhagnsiming Excel验证类
 *
 */
public class ExcelImportUtils {

	// @描述：是否是2003的excel，返回true是2003
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	// @描述：是否是2007的excel，返回true是2007
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 验证EXCEL文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean validateExcel(String filePath) {
		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			return false;
		}
		return true;
	}

	public static boolean isRowEmpty(Row row) {
		for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
			Cell cell = row.getCell(c);
			if (cell != null && cell.getCellType() != CellType._NONE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @description: 获取excel单元格的值
	 * @author:liming
	 */
	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		String cellValue;
		if (cell == null || cell.getCellType() == CellType.BLANK) {
			return "";
		}
		DecimalFormat df = new DecimalFormat("#");
		switch (cell.getCellType()) {
		case STRING:
			cellValue = cell.getRichStringCellValue().getString().trim();
			break;
		case NUMERIC:
			cellValue = df.format(cell.getNumericCellValue());
			break;
		case BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case FORMULA:
			cellValue = cell.getCellFormula();
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

}
