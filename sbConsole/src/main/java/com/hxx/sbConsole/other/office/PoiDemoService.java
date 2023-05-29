package com.hxx.sbConsole.other.office;

import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbcommon.common.office.ExcelWriteHelper;
import com.hxx.sbcommon.common.office.POIBatchWriteUseful;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 10:58:52
 **/
@Slf4j
public class PoiDemoService {

    public static void main(String[] args) {
        try {
            {
                File file = new File("d:/tmp/poidemo.XLSX");
                if (file.exists()) {
                    log.info("已删除：{}", file.getName());
                    file.delete();
                }

                case0(file);
            }
            {
                File file = new File("d:/tmp/poidemo.XLS");
                if (file.exists()) {
                    log.info("已删除：{}", file.getName());
                    file.delete();
                }

                case0(file);
            }
            // 大量写入Excel
            {
                File file = new File("d:/tmp/poidemo-batchInsert.XLS");
                if (file.exists()) {
                    log.info("已删除：{}", file.getName());
                    file.delete();
                }

                case1(file);
            }

        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok!");
    }

    static void case0(File file) throws IOException, InvocationTargetException, IllegalAccessException {
        try (ExcelWriteHelper ewh = new ExcelWriteHelper(file, "data1")) {
            List<String> title = new ArrayList<>();
            {
                title.add("网点名称");
                title.add("分拣线编号");
                title.add("分拣口编号");
                title.add("分拣口名称");
                title.add("下一站");
                title.add("下件扫描");
                title.add("快速补码");
                title.add("快件类型");
                title.add("包牌打印类型");
                title.add("包牌打印值");
                title.add("目的地类型");

                title.add("目的地编码");
                title.add("目的地编码");
                title.add("目的地编码");
                title.add("目的地编码");
                title.add("目的地编码");

                title.add("目的地启用");
                title.add("是否赋能");
                title.add("是否取实际派件员");
                title.add("设备层数");
                title.add("综合物流分拣");
                title.add("赋能段码详情");
            }
            // 写入标题
            ewh.writeTitle(title);
            title.clear();
            {
                title.add("网点名称");
                title.add("分拣线编号");
                title.add("分拣口编号");
                title.add("分拣口名称");
                title.add("下一站");
                title.add("下件扫描");
                title.add("快速补码");
                title.add("快件类型");
                title.add("包牌打印类型");
                title.add("包牌打印值");
                title.add("目的地类型");

                title.add("1段码");
                title.add("2段码");
                title.add("3段码");
                title.add("4段码");
                title.add("派件员");

                title.add("目的地启用");
                title.add("是否赋能");
                title.add("是否取实际派件业务员");
                title.add("设备层数");
                title.add("综合物流分拣");
                title.add("赋能段码详情");
            }
            // 写入标题
            ewh.writeTitle(title);

            // 合并单元格
            List<ExcelWriteHelper.BlockInfo> blockInfos = new ArrayList<>();
            // 合并目的地编码
            blockInfos.add(new ExcelWriteHelper.BlockInfo(0, 0, 11, 15, ""));
            // 合并其他
            for (int i = 0; i < 11; i++) {
                blockInfos.add(new ExcelWriteHelper.BlockInfo(0, 1, i, i, ""));
            }
            for (int i = 16; i < 22; i++) {
                blockInfos.add(new ExcelWriteHelper.BlockInfo(0, 1, i, i, ""));
            }
            ewh.mergeCell(blockInfos);

            List<DemoCls> data = new ArrayList<>();
            {
                for (int i = 0; i < 1000; i++) {
                    DemoCls m = new DemoCls();
                    m.setId(i);
                    m.setCode("code" + i);
                    m.setName("name" + i);
                    data.add(m);
                }
            }
            Map<Integer, String> fieldMap = new HashMap<>();
            fieldMap.put(0, "id");
            fieldMap.put(1, "code");
            fieldMap.put(2, "name");

            List<List<DemoCls>> ls = ListUtils.partition(data, 500);
            for (List<DemoCls> item : ls) {
                ewh.writeDataWithModel(fieldMap, item);
            }
            ewh.saveToFile();
        }
    }

    static void case1(File file) throws Exception {
        Map<Integer, String> fieldMap = new HashMap<>();
        fieldMap.put(0, "id");
        fieldMap.put(1, "code");
        fieldMap.put(2, "name");

        int cou = 0;
        List<DemoCls> data = new ArrayList<>();
        for (int i = 0; i < 2_0000; i++) {
            DemoCls m = new DemoCls();
            m.setId(i);
            m.setCode("code" + i);
            m.setName("name" + i);
            data.add(m);

            if (i > 0 && i % 5000 == 0) {
                cou += write2Excel(file, "data", fieldMap, data);
                data.clear();
            }
        }

        if (!CollectionUtils.isEmpty(data)) {
            cou += write2Excel(file, "data", fieldMap, data);
            data.clear();
        }
        data = null;

        System.out.println("批量生成：" + cou);
    }

    static <T> int write2Excel(File file, String sheetName, Map<Integer, String> fieldMap, List<T> data) {
        try (POIBatchWriteUseful batchWrite = new POIBatchWriteUseful(file, sheetName)) {

            batchWrite.writeDataWithModel(fieldMap, data);
            return data.size();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
