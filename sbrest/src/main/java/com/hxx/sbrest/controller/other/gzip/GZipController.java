package com.hxx.sbrest.controller.other.gzip;

import com.hxx.sbcommon.common.json.JsonUtil;
import com.hxx.sbrest.model.T1Model;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("gzip")
public class GZipController {

    @Autowired
    private BasicTestService basicTestService;

    /**
     * /gzip/getdata
     *
     * @param t
     * @return
     */
    @RequestMapping("/getdata")
    public String getData(@RequestBody T1Model t) {
        return JsonUtil.toJSON(t);
    }

}
