package com.hxx.sbConsole.module.imgproc.compressor;

import com.hxx.sbConsole.commons.util.WebpUtil;
import lombok.var;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;

/**
 * 压缩为Wepb，
 * 推荐使用，压缩效果好
 */
@var
public class WebpCompression {

    public static void main(String[] args) {
        var imgDirPath = "D:\\tmp\\zt-dev\\risk\\img";
        File imgDir = new File(imgDirPath);
        var files = imgDir.listFiles(File::isFile);

        float imageQuality = 0.5f;

        for (File file : files) {
            var inputImagePath = file.getPath();
            try {
                var webpFile = new File(inputImagePath + "-compress.webp");
                try (FileImageOutputStream webpStream = new FileImageOutputStream(webpFile)) {
                    WebpUtil.toWebp(new File(inputImagePath),
                            imageQuality,
                            webpStream);
                }

                System.out.println("图片压缩成功！" + inputImagePath);
            } catch (Exception e) {
                System.out.println("图片压缩失败：" + e.getMessage());
            }
        }
    }
}
