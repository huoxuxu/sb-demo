package com.hxx.sbConsole.service.impl;

import com.hxx.sbConsole.module.easyExcel.EasyExcelDemo2;
import com.hxx.sbcommon.common.io.cfg.ResourcesUtil;
import com.hxx.sbcommon.common.io.json.fastjson.JsonUtil;

import java.io.IOException;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-09-05 15:47:52
 **/
public class CommonDataService {

    /**
     * 获取测试数据
     *
     * @return
     * @throws IOException
     */
    public static List<EasyExcelDemo2.ParkInfo> getData() throws IOException {
        String json = ResourcesUtil.readString("tmp/parkInfo.json");
        List<EasyExcelDemo2.ParkInfo> data = JsonUtil.parseArray(json, EasyExcelDemo2.ParkInfo.class);
        return data;
    }
}
