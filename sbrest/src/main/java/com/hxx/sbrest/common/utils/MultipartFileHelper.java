package com.hxx.sbrest.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-09-09 9:56:35
 **/
@Slf4j
public class MultipartFileHelper {
    private MultipartFile multipartFile;

    private final String fileName;
    private final long fileSize;

    /**
     * @param multipartFile
     */
    public MultipartFileHelper(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;

        this.fileSize = this.multipartFile.getSize();

        String fileName = this.multipartFile.getOriginalFilename();
        if (!StringUtils.isEmpty(fileName)) {
            this.fileName = fileName.trim()
                    .toLowerCase();
        } else {
            this.fileName = "";
        }
    }


    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public InputStream getInputStream() throws IOException {
        return this.multipartFile.getInputStream();
    }
}
