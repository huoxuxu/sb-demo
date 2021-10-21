package com.hxx.sbrest.controller;

import com.hxx.sbrest.common.utils.JsonUtil;
import com.hxx.sbrest.controller.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-04-29 14:57:52
 **/
@RestController
@RequestMapping("rm")
public class RequestMappingController extends BaseController {

    /**
     * /rm/t1/9998/hhhxxx
     * @param ids
     * @param names
     * @return
     */
    @GetMapping("t1/{id}/{name}")
    public String test5(@PathVariable("id") Long ids , @PathVariable("name") String names){
        Map<String,Object> map=new HashMap<>();
        map.put("id",ids);
        map.put("name",names);

        return JsonUtil.toJSON(map);
    }



}