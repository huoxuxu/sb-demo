package com.hxx.sbcommon.common.office;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * POI驱动的写入大量数据的工具类
 * 实例化本类后，调用Write 方法，会提交到Excel 文件中
 * 注意：每次需要实例化本类后调用write 方法，不能实例化一次多次调用writer方法
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-26 12:47:54
 **/
public class POIBatchWriteUseful implements AutoCloseable {

    private File excel;
    Workbook workbook;
    Sheet sheet;
    private int rowIndex;

    public POIBatchWriteUseful(File excel, String sheetName) {
        this.excel = excel;

        // 加载excel
        try {
            this.workbook = POIUtil.getWorkbook(excel, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.sheet = this.workbook.getSheet(sheetName);
        if (this.sheet == null) {
            this.sheet = this.workbook.createSheet(sheetName);
            rowIndex = 0;
        } else {
            rowIndex = this.sheet.getLastRowNum() + 1;
        }
    }


    public Workbook getWorkbook() {
        return this.workbook;
    }

    public Sheet getSheet(int sheetIndex) {
        return this.workbook.getSheetAt(sheetIndex);
    }

    public Sheet getSheet(String sheetName) {
        return this.workbook.getSheet(sheetName);
    }

    @Override
    public void close() throws Exception {
        try {
            if (this.workbook != null) {
                this.workbook.close();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 写入数据, 注意：写入完成后会保存到文件中
     *
     * @param fieldMap 字段映射，key= 字段所在Excel列的索引 val=字段对应T 的属性名
     * @param data
     * @param <T>
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public <T> void writeDataWithModel(Map<Integer, String> fieldMap, List<T> data) throws InvocationTargetException, IllegalAccessException, IOException {
        if (CollectionUtils.isEmpty(data)) return;

        PropertyDescriptor[] props = null;
        for (T item : data) {
            if (props == null) props = BeanUtils.getPropertyDescriptors(item.getClass());

            Map<String, Object> valMap = new HashMap<>();
            for (PropertyDescriptor prop : props) {
                //得到属性的name
                String key = prop.getName();
                if (!fieldMap.containsValue(key)) {
                    continue;
                }
                Method getMethod = prop.getReadMethod();
                if (getMethod == null) {
                    continue;
                }

                //执行get方法得到属性的值
                Object value = getMethod.invoke(item);
                valMap.put(key, value);
            }

            List<String> vals = new ArrayList<>();
            for (int i = 0; i < fieldMap.size(); i++) {
                String field = fieldMap.get(i);
                Object val = valMap.getOrDefault(field, null);
                if (val == null) {
                    val = "";
                }
                vals.add(val + "");
            }

            // 写入Excel
            Row row = this.sheet.createRow(this.rowIndex);
            this.rowIndex++;
            POIUtil.writeRow(row, vals);
        }

        // 写入文件
        POIUtil.saveToFile(this.workbook, this.excel);
    }

    /**
     * 导入Excel，注意：写入完成后会保存到文件中
     *
     * @param fieldMap 字段映射，key= 字段所在Excel列的索引 val=字段对应T 的属性名
     * @param data
     * @throws IOException
     */
    public void writeData(Map<Integer, String> fieldMap, List<Map<String, Object>> data) throws IOException {
        if (CollectionUtils.isEmpty(data)) return;

        for (Map<String, Object> valMap : data) {
            List<String> vals = new ArrayList<>();
            for (int i = 0; i < fieldMap.size(); i++) {
                String field = fieldMap.get(i);
                Object val = valMap.getOrDefault(field, null);
                if (val == null) {
                    val = "";
                }
                vals.add(val + "");
            }

            // 写入Excel
            Row row = this.sheet.createRow(this.rowIndex);
            this.rowIndex++;
            POIUtil.writeRow(row, vals);
        }

        // 写入文件
        POIUtil.saveToFile(this.workbook, this.excel);
    }

}
