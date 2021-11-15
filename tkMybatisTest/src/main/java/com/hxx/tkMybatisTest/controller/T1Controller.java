package com.hxx.tkMybatisTest.controller;

import com.hxx.sbcommon.common.JsonUtil;
import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import com.hxx.tkMybatisTest.service.T1Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-11-12 15:29:10
 **/
@RequestMapping("t1")
@RestController
public class T1Controller {
    @Autowired
    private T1Service t1Service;


    @RequestMapping("get")
    public String get(String data) {
        List<T1> all = t1Service.getAll(data);
        return JsonUtil.toJSON(all);
    }

    @RequestMapping("select")
    public String get(String msgType, @RequestBody String data) {
        if (StringUtils.isEmpty(msgType)) {
            return "msgType 不能为空！";
        }

        msgType = msgType.trim().toUpperCase();
        switch (msgType) {
            case "SELECT_BY":
                // http://localhost:8083/t1/select?msgType=select_by&data=%7b%22code%22%3a%22%22%2c%22name%22%3a%22ahxx%22%7d
                T1 t1 = JsonUtil.parse(data, T1.class);
                List<T1> all = t1Service.selectBy(t1);
                return JsonUtil.toJSON(all);
            default:
                break;
        }

        return "{}";
    }

}
