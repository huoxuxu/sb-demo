package com.hxx.sbConsole.service.impl.demo.office;

import com.hxx.sbConsole.module.easyExcel.EasyExcelDemo2;
import com.hxx.sbConsole.service.impl.CommonDataService;
import com.hxx.sbcommon.common.io.cfg.ResourcesUtil;
import com.hxx.sbcommon.common.io.fileOrDir.FileUtil;
import com.hxx.sbcommon.common.office.poi.POIExcelUseful;
import com.hxx.sbcommon.common.office.poi.POIExcelWriteUseful;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.ReflectUseful;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-02 11:23:23
 **/
public class OfficeDemoService {
    public static void main(String[] args) {
        try {
            // 创建空excel
            case2();

//            // 写入Excel
//            case0();
//            // 读取Excel
//            case1();


        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("ok!");
    }

    // 写入文件
    static void case0() throws Exception {
        String fileName = "d:/tmp/poi-write-demo.xlsx";
        File excel = new File(fileName);

        ReflectUseful reflectUseful = new ReflectUseful(EasyExcelDemo2.ParkInfo.class);
        List<String> titles = reflectUseful.getProps();
        Map<Integer, String> fieldMap = new HashMap<>();
        for (int i = 0; i < titles.size(); i++) {
            String title = titles.get(i);
            fieldMap.put(i, title);
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        List<EasyExcelDemo2.ParkInfo> data = CommonDataService.getData();
        for (EasyExcelDemo2.ParkInfo item : data) {
            rows.add(BeanInfoUtil.toMap(item));
        }

        try (POIExcelWriteUseful writeUseful = new POIExcelWriteUseful(excel, "data")) {
            // 写标题
            writeUseful.writeTitle(titles);
            // 写数据
            writeUseful.writeData(fieldMap, rows);
            // 保存到Excel文件中
            writeUseful.saveToFile();
        }
        System.out.println("ok");
    }

    // 解析文件
    static void case1() throws Exception {
        String fileName = "d:/tmp/poi-write-demo.xlsx";
        File excel = new File(fileName);
        InputStream iptStream = FileUtil.getFileStream(excel);
        try (POIExcelUseful POIExcelUseful = new POIExcelUseful(fileName, iptStream)) {
            POIExcelUseful.parseExcelRows(0, 0, 9, row -> {
                System.out.println(String.join(" ", row.getItemArray()));
            });
        }
    }

    // 创建空文件
    static void case2() throws IOException {
        {
            String fileName = "d:/tmp/poi-write-demo-new.xls";
            File excel = new File(fileName);
            if (excel.exists()) {
                excel.delete();
            }

            try (POIExcelWriteUseful writeUseful = new POIExcelWriteUseful(excel, "data")) {
                // 保存到Excel文件中
                writeUseful.saveToFile();
            }
        }
        {
            String fileName = "d:/tmp/poi-write-demo-new.xlsx";
            File excel = new File(fileName);
            if (excel.exists()) {
                excel.delete();
            }

            try (POIExcelWriteUseful writeUseful = new POIExcelWriteUseful(excel, "data")) {
                // 保存到Excel文件中
                writeUseful.saveToFile();
            }
        }
    }

}
