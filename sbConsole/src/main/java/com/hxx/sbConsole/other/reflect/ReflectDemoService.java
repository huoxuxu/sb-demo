package com.hxx.sbConsole.other.reflect;

import com.alibaba.excel.annotation.ExcelProperty;
import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbConsole.model.HaBaDog;
import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbcommon.common.office.EasyExcelHelper;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import com.hxx.sbcommon.common.reflect.ReflectUseful;
import com.hxx.sbcommon.common.reflect.ReflectorObj;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-25 13:31:40
 **/
public class ReflectDemoService {
    public static void main(String[] args) {
        try {
            EasyExcelHelper.DemoEasyExcelVO demoVO = new EasyExcelHelper.DemoEasyExcelVO();
            demoVO.setCode("ccd");
            demoVO.setName("nnm");
            Map<String, Object> map = BeanInfoUtil.toMap(demoVO);

            Class<DemoCls> type = DemoCls.class;
            ReflectUseful reflectUseful = new ReflectUseful(HaBaDog.class);
            List<String> props = reflectUseful.getProps();
            System.out.println("props: " + JsonUtil.toJSON(props));


            ReflectorObj reflectorObj = new ReflectorObj(type);
            List<String> fields = reflectorObj.getClsGetterFields();
            for (int i = 0; i < fields.size(); i++) {
                String prop = fields.get(i);
                System.out.println("fieldMap.put(" + i + ", \"" + prop + "\");");
            }

            System.out.println(map);
        } catch (Exception ex) {
            System.out.println(ex + "");
        }
        System.out.println("ok");
    }

}
