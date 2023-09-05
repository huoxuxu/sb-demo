package com.hxx.sbConsole.module.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.annotation.JSONField;
import com.hxx.sbConsole.service.impl.CommonDataService;
import com.hxx.sbcommon.common.office.easyExcel.EasyExcelReadHelper;
import com.hxx.sbcommon.common.office.easyExcel.EasyExcelWriteHelper;
import lombok.Data;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-30 16:19:25
 **/
public class EasyExcelDemo2 {
    public static void main(String[] args) {
        try {
            List<ParkInfo> data = CommonDataService.getData();
            // 写入Excel
            String excelPath = "d:/tmp/easyexcelReadWriteDemo.xls";
            File excelFile = new File(excelPath);
            if (excelFile.exists()) {
                FileUtils.delete(excelFile);
            }

            try (EasyExcelWriteHelper writeHelper = new EasyExcelWriteHelper(excelPath)) {
                List<List<ParkInfo>> parts = ListUtils.partition(data, 10);
                // 工作簿
                WriteSheet sheet = writeHelper.createSheet(0, "data", ParkInfo.class);
                for (List<ParkInfo> part : parts) {
                    writeHelper.writeSheet(sheet, part);
                }
                data.get(0).platformId = "hh";
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
        @ExcelProperty(value = "月台")
        private String platformId;

        @ExcelProperty(value = "使用数")
        private int useTotal;

        @ExcelProperty(value = "使用人数")
        private long peopleTotal;

        @ExcelProperty(value = "使用量")
        private BigDecimal total = BigDecimal.TEN;

        @ExcelProperty(value = "负荷")
        private double loadRate = 99.86D;

        @ExcelProperty(value = "启用")
        private boolean enabled;

        @ExcelProperty(value = "创建时间")
        private Date createTime = new Date();

        @ExcelProperty(value = "更新时间")
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updateTime = LocalDateTime.now();

        @ExcelProperty(value = "备注")
        private String remark = null;
    }
}
