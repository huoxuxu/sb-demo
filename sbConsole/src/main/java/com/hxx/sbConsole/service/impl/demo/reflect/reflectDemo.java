package com.hxx.sbConsole.service.impl.demo.reflect;

import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbcommon.common.reflect.BeanInfoUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;


/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-12 16:13:42
 **/
public class reflectDemo {
    public static void main(String[] args) {
        try {
            DemoCls.BasicTypeCls t = new DemoCls.BasicTypeCls();
            // 将字段中的包装类型去null处理
            BeanInfoUtil.procFieldNull(t);
            System.out.println(t);
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    static void case0() {

    }

}
