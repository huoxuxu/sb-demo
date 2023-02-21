package com.hxx.sbweb.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-11-15 16:58:56
 **/
@Data
public class Chunk {
    /**
     * 当前切片号
     */
    private int chunkNumber;
    /**
     * 总切片数
     */
    private int totalChunks;

    private String filename;
    private MultipartFile file;
    /**
     * 当前切片大小
     */
    private long currentChunkSize;
}
