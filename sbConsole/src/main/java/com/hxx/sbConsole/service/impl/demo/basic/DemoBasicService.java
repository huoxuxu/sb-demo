package com.hxx.sbConsole.service.impl.demo.basic;

import com.hxx.sbConsole.model.DemoCls;
import com.hxx.sbConsole.model.validate.UserModel;
import com.hxx.sbcommon.common.basic.validate.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.time.LocalDateTime;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-03-23 12:53:17
 **/
@Slf4j
public class DemoBasicService {
    public static void main(String[] args) {
        try {
            // 包含基本类型的类
            DemoCls demoCls = new DemoCls();
            {
                boolean enabled = demoCls.isEnabled();
                demoCls.setEnabled(true);
            }
            DemoCls.BasicTypeCls basicTypeCls = new DemoCls.BasicTypeCls();

            case0();

            System.out.println("ok!");
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    static void case0() {
        System.out.println("==================================================");
        {
            UserModel u = new UserModel();
            {
                u.setId(1);
                u.setCode("P12345");
                u.setBirthday(LocalDateTime.MIN);
                u.setScore(100F);
                u.setEnabled(false);
            }
            String err = ValidateUtil.validateModel(u);
            System.out.println(err);
        }
    }


}
