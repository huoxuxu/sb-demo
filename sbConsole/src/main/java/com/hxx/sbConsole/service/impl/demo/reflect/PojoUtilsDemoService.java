package com.hxx.sbConsole.service.impl.demo.reflect;

import com.google.common.collect.ImmutableMap;
import com.hxx.sbConsole.model.ExportParam;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.dubbo.common.utils.PojoUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-04-20 8:56:55
 **/
public class PojoUtilsDemoService {

    public static void main(String[] args) {
        try{
            // 报错
            testRealize();


        }catch (Exception ex){
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
        System.out.println("ok");
    }

    // 强大的参数解析类 com.alibaba.dubbo.common.utils.PojoUtils，
    // 用于将嵌套的 Map 转换成嵌套的对象。特别实用。
    // PoJoUtils 与 CompatibleTypeUtils, ReflectUtils, ClassHelper 联合实现了这个功能
    static void testRealize() {
        ExportParam exportParam = new ExportParam();
        exportParam.setBizType("order");
        exportParam.setCategory("baozhengjin");
        exportParam.setSource("wsc");
        exportParam.setRequestId("request" + System.currentTimeMillis());
        exportParam.setExtra(ImmutableMap.of("account", "qin"));
        exportParam.setStrategy("standard");
        ExportParam.SearchParam searchParam = new ExportParam.SearchParam();
        searchParam.setBizId(123L);
        searchParam.setStartTime(151456798L);
        searchParam.setEndTime(153456789L);
        // ！！！貌似不支持枚举设置！！！
//        searchParam.setConditions(Arrays.asList(
//                new ExportParam.Condition("name", ExportParam.Op.eq, "sisi"),
//                new ExportParam.Condition("pay_time", ExportParam.Op.range,
//                        new ExportParam.Range(152987654L, 153987654)),
//                new ExportParam.Condition("state", ExportParam.Op.in, Arrays.asList(5, 6)),
//                new ExportParam.Condition("goods_title", ExportParam.Op.match,
//                        new ExportParam.Match("走过路过不要错过", "100%"))
//        ));
        exportParam.setSearch(searchParam);
        // 对象转成Map
        Map<String,Object> paramGeneralized = (Map<String,Object>)PojoUtils.generalize(exportParam);
        // Map转对象
        ExportParam exportParamRestored = (ExportParam) PojoUtils.realize(paramGeneralized, ExportParam.class);
        assert Objects.equals(exportParamRestored, exportParam);

    }
}
