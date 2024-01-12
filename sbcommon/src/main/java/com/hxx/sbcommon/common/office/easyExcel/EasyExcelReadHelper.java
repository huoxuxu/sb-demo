package com.hxx.sbcommon.common.office.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.apache.commons.collections4.CollectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EasyExcel 读取工作簿内容
 *
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-08-30 16:03:34
 **/
public class EasyExcelReadHelper implements AutoCloseable {

    private InputStream excelInputStream;
    private final ExcelReader excelReader;

    private final AnalysisEventListenerImpl<Object> listener = new AnalysisEventListenerImpl<>();

    public EasyExcelReadHelper(String excelPath) throws IOException {
        this.excelInputStream = Files.newInputStream(Paths.get(excelPath));
        this.excelReader = EasyExcel.read(this.excelInputStream, listener).build();
    }

    public EasyExcelReadHelper(InputStream excelInputStream) {
        this.excelReader = EasyExcel.read(excelInputStream, listener).build();
    }

    /**
     * 读取工作簿数据
     *
     * @param sheetIndex
     * @param voCls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> readSheet(int sheetIndex, Class voCls) {
        // 清理读取器中的数据
        listener.getData().clear();

        // 第一个sheet读取类型
        ReadSheet readSheet1 = EasyExcel.readSheet(sheetIndex)
                .head(voCls)
                .build();
        // 开始读取第一个sheet
        excelReader.read(readSheet1);
        List<Object> data = listener.getData();
        if (CollectionUtils.isEmpty(data)) return new ArrayList<>();

        return data.stream()
                .map(d -> (T) d)
                .collect(Collectors.toList());
    }

    @Override
    public void close() throws Exception {
        if (this.excelReader != null) excelReader.finish();
        if (this.excelInputStream != null) this.excelInputStream.close();
    }

    public static class AnalysisEventListenerImpl<T> extends AnalysisEventListener<T> {

        private final List<T> data = new ArrayList<>();

        public List<T> getData() {
            return data;
        }

        // 在读取完一行数据后调用
        @Override
        public void invoke(T t, AnalysisContext analysisContext) {
            data.add(t);
            System.out.println(t);
        }

        // 在读取完所有数据后调用
        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            System.out.println("读取完毕");
        }
    }

}
