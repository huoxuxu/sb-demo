package com.hxx.sbConsole.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-20 13:14:59
 **/
@Service
public class Demo {
    @Value("作者")
    private String anothor;
    public String getAnothor() {
        return anothor;
    }
    public void setAnothor(String anothor) {
        this.anothor = anothor;
    }

}
