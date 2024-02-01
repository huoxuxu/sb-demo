package com.hxx.sbrest.controller.proj;

import com.hxx.sbrest.webUtil.WebProjPowerful;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("proj")
public class ProjController {

    @Autowired
    private WebProjPowerful webProjPowerful;

    /**
     * 查询项目暴漏的所Url
     *
     * @return
     */
    @RequestMapping("/getAllUrls")
    public Collection<String> getT1() {
        Set<String> allUrls = webProjPowerful.getAllUrl();
        return allUrls.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
