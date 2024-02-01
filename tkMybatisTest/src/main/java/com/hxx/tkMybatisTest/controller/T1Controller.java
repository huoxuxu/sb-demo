package com.hxx.tkMybatisTest.controller;

import com.hxx.sbcommon.common.io.json.JsonUtil;
import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1;
import com.hxx.tkMybatisTest.dal.mysql.testDB.entity.T1Example;
import com.hxx.tkMybatisTest.service.T1Service;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    public String get(HttpServletRequest request,
                      String msgType, @RequestBody(required = false) String data) {
        if (StringUtils.isEmpty(msgType)) {
            return "msgType 不能为空！";
        }
        if (StringUtils.isEmpty(data)) {
            data = request.getParameter("data");

            if (StringUtils.isEmpty(data)) {
                return "data 不能为空！";
            }
        }

        msgType = msgType.trim().toUpperCase();
        switch (msgType) {
            case "SELECT_BY": {
                // http://localhost:8083/t1/select?msgType=select_by&data=%7b%22code%22%3a%22%22%2c%22name%22%3a%22ahxx%22%7d
                T1Example t1 = JsonUtil.parse(data, T1Example.class);
                List<T1> all = t1Service.selectBy(t1);
                return JsonUtil.toJSON(all);
            }
            case "INSERT_DEMO": {
                List<T1> ls = JsonUtil.parseArray(data, T1.class);
                if (CollectionUtils.isEmpty(ls)) {
                    return "0";
                }

                Integer result = t1Service.insertDemo(ls);
                return result == null ? "0" : result + "";
            }
            case "UPDATE_DEMO": {
                T1 t1 = JsonUtil.parse(data, T1.class);
                Integer result = t1Service.updateDemo(t1);
                return result == null ? "0" : result + "";
            }
            case "DELETE_DEMO": {
                List<Integer> ids = JsonUtil.parseArray(data, Integer.class);
                if (CollectionUtils.isEmpty(ids)) {
                    return "0";
                }

                Integer result = t1Service.deleteDemo(ids);
                return result == null ? "0" : result + "";
            }
            default:
                return "{}";
        }
    }

}
