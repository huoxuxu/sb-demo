package com.hxx.sbConsole.module.easyExcel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-03 13:52:09
 **/
@Slf4j
public class EasyExcelDynamicHeaderDemo {

    public static void main(String[] args) {
        try {
            String path = "d:/tmp/easyExcelDynamicHeaderDemo.xls";
            List<List<String>> dynamicHeaders = myHead();
            List<List<Object>> data = myData();

            EasyExcelUtil.generateExcelWithDynamicHeader(path, dynamicHeaders, data);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("ok!");
    }

    private static List<List<String>> myHead() {
        List<List<String>> list = new ArrayList<>();

        Field[] selfFields = MyDemoData.class.getDeclaredFields();
        Field[] supperFields = MyDemoData.class.getSuperclass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(Arrays.asList(supperFields));
        fieldList.addAll(Arrays.asList(selfFields));

        for (Field field : fieldList) {
            ExcelIgnore annotation = field.getAnnotation(ExcelIgnore.class);
            if (null != annotation) continue;

            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            if (null == excelProperty) continue;

            String[] excelPropertyDesc = excelProperty.value();
            if (excelPropertyDesc != null) {
                List<String> excelProps = Arrays.stream(excelPropertyDesc).filter(d -> !StringUtils.isBlank(d)).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(excelProps)) {
                    list.add(excelProps);
                }
            }
        }
        // 动态字段
        list.add(Arrays.asList("扩展字段1"));
        list.add(Arrays.asList("扩展字段2"));
        return list;
    }

    private static List<List<Object>> myData() {
        // 假设从db拿到的数据
        MyDemoData data = new MyDemoData();
        data.setId(0);
        data.setName("和黑");
        // 动态扩展字段
        data.setExtColumnList(Arrays.asList("ext1", "ext2"));
        List<MyDemoData> dataList = Arrays.asList(data);

        Field[] selfFields = MyDemoData.class.getDeclaredFields();
        Field[] supperFields = MyDemoData.class.getSuperclass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<>(Arrays.asList(supperFields));
        fieldList.addAll(Arrays.asList(selfFields));

        List<List<Object>> list = new ArrayList<>();
        for (MyDemoData myData : dataList) {
            List<Object> colData = new ArrayList<>();
            for (Field field : fieldList) {
                ExcelIgnore annotation = field.getAnnotation(ExcelIgnore.class);
                if (null != annotation) continue;

                field.setAccessible(true);
                try {
                    Object obj = field.get(myData);
                    if (obj instanceof List) {
                        colData.addAll((List<?>) obj);
                    } else {
                        colData.add(obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            list.add(colData);
        }
        return list;
    }

    @Data
    static class MyDemoData {
        @ExcelProperty(value = "艾迪")
        private int id;
        @ExcelProperty(value = "内木")
        private String name;

        //        @ExcelIgnore
        @ExcelProperty
        private List<Object> extColumnList;
    }

}
