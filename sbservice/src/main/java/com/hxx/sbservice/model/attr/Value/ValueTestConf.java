package com.hxx.sbservice.model.attr.Value;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-08-30 14:09:04
 **/
@Data
@Component
public class ValueTestConf {

    @Value("#{1}")
    private int number; //获取数字 1

    // 获取字符串常量
    @Value("#{'Spring Expression Language'}")
    private String str;

    // 注入普通字符串
    @Value("normal")
    private String normal;

    // 注入操作系统属性
    @Value("#{systemProperties['os.name']}")
    private String systemPropertiesName;

    //注入表达式结果
    @Value("#{ T(java.lang.Math).random() * 100.0 }")
    private double randomNumber;

    //获取bean的属性
    @Value("#{newJdbcModel.userName}")
    private String userName;

    @Value("${jdbc.userName}")
    private String userName2;

}
