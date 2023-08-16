package com.hxx.sbConsole.service.impl.demo.office;

import com.hxx.sbcommon.common.io.FileUtil;
import com.hxx.sbcommon.common.office.ExcelHelper;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-02 11:23:23
 **/
public class OfficeDemoService {
    public static void main(String[] args) {
        try {
            String fileName = "d:/tmp/demo.xlsx";
//            fileName="C:\\Users\\huoxuxu\\Desktop\\11\\123.xls";
            InputStream iptStream = FileUtil.getFileStream(new File(fileName));
            try (ExcelHelper excelHelper = new ExcelHelper(fileName, iptStream)) {
                excelHelper.parseExcelRows(0, 1, 4, row -> {
                    System.out.println(String.join(" ", row.getItemArray()));
                });
            }

        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("ok!");
    }


}
