package com.hxx.sbConsole.module.easyExcel.handler;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;

import java.util.List;

import com.hxx.sbConsole.module.easyExcel.EasyExcelDemo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-10 11:15:07
 **/
public class ExcelFillCelMergeStrategy implements CellWriteHandler {

    //自定义合并单元格的列 如果想合并   第4列和第5例 、第6列和第7例： [CellLineRange(firstCol=3, lastCol=4), CellLineRange(firstCol=5, lastCol=6)]
    private List<EasyExcelDemo.CellLineRange> cellLineRangeList;

    //自定义合并单元格的开始的行  一般来说填表头行高0 表示从表头下每列开始合并 ：如表头行高位为3则 int mergeRowIndex = 2  ;
    private int mergeRowIndex;

    public ExcelFillCelMergeStrategy(List<EasyExcelDemo.CellLineRange> cellLineRangeList, int mergeRowIndex) {
        this.cellLineRangeList = cellLineRangeList;
        this.mergeRowIndex = mergeRowIndex;
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

        //当前单元格的行数
        int curRowIndex = cell.getRowIndex();
        // 当前单元格的列数
        int curColIndex = cell.getColumnIndex();
        if (curRowIndex > mergeRowIndex) {
            if (curRowIndex > mergeRowIndex) {
                for (int i = 0; i < cellLineRangeList.size(); i++) {
                    if (curColIndex > cellLineRangeList.get(i)
                            .getFirstCol() && curColIndex <= cellLineRangeList.get(i)
                            .getLastCol()) {
                        //单元格数据处理
                        mergeWithLeftLine(writeSheetHolder, cell, curRowIndex, curColIndex);
                        break;
                    }
                }
            }
        }
    }

    /**
     * @description 当前单元格向左合并
     */
    private void mergeWithLeftLine(WriteSheetHolder writeSheetHolder, Cell cell, int curRowIndex, int curColIndex) {
        //当前单元格中数据
        Object curData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();
        //获取当前单元格的左面一个单元格
        Cell leftCell = cell.getSheet()
                .getRow(curRowIndex)
                .getCell(curColIndex - 1);
        //获取当前单元格的左面一个单元格中的数据
        Object leftData = leftCell.getCellTypeEnum() == CellType.STRING ? leftCell.getStringCellValue() : leftCell.getNumericCellValue();

        // 将当前单元格数据与左侧一个单元格数据比较
        if (leftData.equals(curData)) {
            //获取当前sheet页
            Sheet sheet = writeSheetHolder.getSheet();
            //得到所有的合并单元格
            List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
            //是否合并
            boolean isMerged = false;
            for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                //CellRangeAddress POI合并单元格
                //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                //例子：CellRangeAddress（2, 6000, 3, 3）；
                //第2行起 第6000行终止 第3列开始 第3列结束。
                CellRangeAddress cellRangeAddr = mergeRegions.get(i);
                // cellRangeAddr.isInRange(int rowInd, int colInd)确定给定坐标是否在此范围的范围内。
                // 若左侧一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                if (cellRangeAddr.isInRange(curRowIndex, curColIndex - 1)) {
                    sheet.removeMergedRegion(i);
                    cellRangeAddr.setLastColumn(curColIndex);
                    sheet.addMergedRegion(cellRangeAddr);
                    isMerged = true;
                }
            }
            // 若左侧一个单元格未被合并，则新增合并单元
            if (!isMerged) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(curRowIndex, curRowIndex, curColIndex - 1, curColIndex);
                sheet.addMergedRegion(cellRangeAddress);
            }
        }

    }

}
