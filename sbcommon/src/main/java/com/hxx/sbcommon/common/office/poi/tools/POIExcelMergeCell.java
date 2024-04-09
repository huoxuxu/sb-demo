package com.hxx.sbcommon.common.office.poi.tools;

import com.hxx.sbcommon.common.office.poi.POIExcelWriteUseful;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2024-01-23 9:18:04
 **/
public class POIExcelMergeCell {

    private final Sheet sheet;

    public POIExcelMergeCell(Sheet sheet) {
        this.sheet = sheet;
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
    public void mergeCell(CellBlockInfo blockInfo) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(
                blockInfo.getStartRowIndex(),
                blockInfo.getEndRowIndex(),
                blockInfo.getStartCellIndex(),
                blockInfo.getEndCellIndex());
        // 合并
        sheet.addMergedRegion(cellRangeAddress);
    }

    /**
     * 合并单元格
     *
     * @param blockInfos
     */
    public void mergeCell(List<CellBlockInfo> blockInfos) {
        if (CollectionUtils.isEmpty(blockInfos)) {
            return;
        }

        for (CellBlockInfo item : blockInfos) {
            mergeCell(item);
        }
    }


    /**
     * 合并单元格信息
     */
    @lombok.Data
    public static class CellBlockInfo {
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

        public CellBlockInfo() {
        }

        public CellBlockInfo(int startRowIndex, int endRowIndex, int startCellIndex, int endCellIndex) {
            this(startRowIndex, endRowIndex, startCellIndex, endCellIndex, null);
        }

        public CellBlockInfo(int startRowIndex, int endRowIndex, int startCellIndex, int endCellIndex, Object cellVal) {
            this.startRowIndex = startRowIndex;
            this.endRowIndex = endRowIndex;
            this.startCellIndex = startCellIndex;
            this.endCellIndex = endCellIndex;

            this.cellVal = cellVal;
        }

    }
}
