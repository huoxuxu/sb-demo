package com.hxx.sbrest.controller;

import com.hxx.sbrest.controller.base.BaseController;
import com.hxx.sbrest.model.Chunk;
import com.hxx.sbrest.model.UploadParam;
import com.hxx.sbrest.service.BasicTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

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
    @Autowired
    MultipartResolver multipartResolver;

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

    @PostMapping(value = "/multiSortPlanUpload")
    public String planUpload(@RequestParam(value = "file") MultipartFile file) {
        return "";
    }

    /**
     * 大文件切片上传
     *
     * @param multipartFile
     * @param chunk
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @PostMapping("bigfile")
    public String bigFileUpload(@RequestParam(value = "file", required = false) MultipartFile multipartFile, @ModelAttribute Chunk chunk, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("第 " + chunk.getChunkNumber() + " 片文件，文件大小：" + chunk.getCurrentChunkSize());
        File file = new File("D:\\1-program\\" + chunk.getFilename());
        if (chunk.getChunkNumber() == 1 && !file.exists()) {
            file.createNewFile();
        }
        try (
                //将块文件写入文件中
                InputStream fos = chunk.getFile()
                        .getInputStream();
                RandomAccessFile raf = new RandomAccessFile(file, "rw")
        ) {
            int len = -1;
            byte[] buffer = new byte[1024];
            raf.seek((chunk.getChunkNumber() - 1) * 1024 * 1024 * 2);
            while ((len = fos.read(buffer)) != -1) {
                raf.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (chunk.getChunkNumber() == chunk.getTotalChunks()) {
            response.setStatus(200);
            // TODO 向数据库中保存上传信息
            return "over";
        } else {
            response.setStatus(201);
            return "ok";
        }
    }

}
