package com.hxx.sbcommon.common.io.fileOrDir.sliceUpload.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-10-21 13:55:17
 **/
@Data
public class FileUploadRequestDTO implements Serializable {
    private static final long serialVersionUID = -829014701521563623L;

    private MultipartFile file;
    private Long chunkSize;

}
