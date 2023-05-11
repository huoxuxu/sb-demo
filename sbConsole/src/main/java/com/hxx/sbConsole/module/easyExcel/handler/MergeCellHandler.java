package com.hxx.sbConsole.module.easyExcel.handler;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.hxx.sbcommon.common.office.ExcelHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Set;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-10 17:08:15
 **/
@Slf4j
public class MergeCellHandler implements CellWriteHandler {
    /**
     * 从第几行开始合并，表头下标为0
     */
    private int mergeStartRowIndex;

    /**
     * 需要合并列的下标，从0开始
     */
    private Set<Integer> mergeColumnIndex;

    /**
     * @param mergeStartRowIndex 开始合并的行
     * @param mergeColumnIndex   需要合并的列
     */
    public MergeCellHandler(int mergeStartRowIndex, Set<Integer> mergeColumnIndex) {
        this.mergeStartRowIndex = mergeStartRowIndex;
        this.mergeColumnIndex = mergeColumnIndex;
    }

    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        //当前行
        int curRowIndex = cell.getRowIndex();

        if (curRowIndex < mergeStartRowIndex + 1) {
            return;
        }

        //当前列
        int curColIndex = cell.getColumnIndex();
        if (mergeColumnIndex.contains(curColIndex)) {
            mergeWithPreRow(writeSheetHolder, cell, curRowIndex, curColIndex);
        }
    }

    // 根据上一行数据绝对是否合并列
    private void mergeWithPreRow(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {
        //获取当前行的当前列的数据和上一行的当前列列数据，通过上一行数据是否相同进行合并
        Row preRow = getRow(writeSheetHolder, curRowIndex - 1);
        if (preRow == null) {
            log.warn("未获取到行，行索引：{}", curRowIndex - 1);
            return;
        }

        if (curColIndex > 1) {
            // 合并前判断是否可以合并
            Row curRow = cell.getRow();
            String curInterfCode = getCellVal(curRow, 1);
            String preInterfCode = getCellVal(preRow, 1);
            if (StringUtils.isBlank(curInterfCode) || StringUtils.isBlank(preInterfCode) || !curInterfCode.equals(preInterfCode)) {
                return;
            }
        }

        // 当前单元格数据
        Cell preCell = preRow.getCell(curColIndex);
        if (preCell == null) {
            log.warn("未获取到列，行索引：{} 列索引：{}", curRowIndex - 1, curColIndex);
            return;
        }

        // 当前单元格的数据
        String curData = getCellVal(cell);
        // 获取上一行单元格数据
        String preData = getCellVal(preCell);
        // 比较当前行的第一列的单元格与上一行是否相同，相同合并当前单元格与上一行
        if (StringUtils.isBlank(curData) || StringUtils.isBlank(preData) || !curData.equals(preData)) {
            return;
        }

        try {
            Sheet sheet = writeSheetHolder.getSheet();
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            boolean isMerged = false;
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellRangeAddr.isInRange(curRowIndex - 1, curColIndex)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastRow(curRowIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }

            // 若上一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex - 1, curRowIndex, curColIndex, curColIndex);
                sheet.addMergedRegion(cellRangeAddress);
            }
        } catch (Exception ex) {
            log.error("出现异常：{}", ExceptionUtils.getStackTrace(ex));
        }
    }

    private Row getRow(WriteSheetHolder writeSheetHolder, int rowIndex) {
        Sheet sheet = writeSheetHolder.getSheet();
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            // 当获取不到上一行数据时，使用缓存sheet中数据
            Sheet cachedSheet = writeSheetHolder.getCachedSheet();
            row = cachedSheet.getRow(rowIndex);
        }
        return row;
    }

    private String getCellVal(Cell cell) {
        try {
            String cellValue = ExcelHelper.getCellValue(cell);
            return cellValue;
        } catch (Exception ex) {
            return null;
        }
    }

    private String getCellVal(Row row, int cellNumber) {
        Cell cell = row.getCell(cellNumber + 1);
        if (cell == null) {
            return null;
        }
        return getCellVal(cell);
    }

}
