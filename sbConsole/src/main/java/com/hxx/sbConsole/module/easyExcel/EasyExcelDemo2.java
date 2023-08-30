package com.hxx.sbConsole.module.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.hxx.sbcommon.common.io.FileUtil;
import com.hxx.sbcommon.common.io.cfg.ResourcesUtil;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.office.easyExcel.EasyExcelReadHelper;
import com.hxx.sbcommon.common.office.easyExcel.EasyExcelWriteHelper;
import lombok.Data;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-30 16:19:25
 **/
public class EasyExcelDemo2 {
    public static void main(String[] args) {
        try {
            String json = ResourcesUtil.readString("tmp/parkInfo.json");
            // 写入Excel
            String excelPath = "d:/tmp/easyexcelReadWriteDemo.xls";
            File excelFile = new File(excelPath);
            if (excelFile.exists()) {
                FileUtils.delete(excelFile);
            }
            List<ParkInfo> data = JsonUtil.parseArray(json, ParkInfo.class);

            try (EasyExcelWriteHelper writeHelper = new EasyExcelWriteHelper(excelPath)) {
                writeHelper.writeSheet(0, "data", data, ParkInfo.class);
                data.get(0).parkId = "hh";
                writeHelper.writeSheet(1, "data1", data, ParkInfo.class);
            }

            // 读取Excel
            try (EasyExcelReadHelper readHelper = new EasyExcelReadHelper(excelPath)) {
                List<ParkInfo> objects = readHelper.readSheet(0, ParkInfo.class);
                System.out.println(objects);

                List<ParkInfo> objects2 = readHelper.readSheet(1, ParkInfo.class);
                System.out.println(objects2);
            }

        } catch (Exception e) {
            System.out.println(e + "");
        }
        System.out.println("ok");
    }

    @Data
    public static class ParkInfo {
        @ExcelProperty(value = "园区")
        private String parkId;

        @ExcelProperty(value = "仓")
        private String buildId;

        @ExcelProperty(value = "楼层")
        private String florId;

        @ExcelProperty(value = "区域")
        private String roomId;

        @ExcelProperty(value = "月台")
        private String platformId;

    }
}
