package com.hxx.sbrest.controller;

import com.hxx.sbrest.controller.base.BaseController;
import com.hxx.sbrest.model.UploadParam;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2021-05-07 17:40:20
 **/
@Slf4j
@RestController
@RequestMapping("upload")
public class UploadTestController extends BaseController {

    @Autowired
    private BasicTestService basicTestService;

    @RequestMapping("/switchTest")
    public String switchTest(String str) {
        log.info("请求参数：{} {{}}{{++--}}", str);
        String str1 = basicTestService.switchTest(str);
        return ok(str1);
    }

    /**
     * 上传文件
     *
     * @param file
     * @param pingDuoDuoImportParam
     * @return
     */
    @PostMapping("/upload")
    public String importScanData(@RequestParam(value = "upload") MultipartFile file, UploadParam pingDuoDuoImportParam) {

        return "";
    }

}
