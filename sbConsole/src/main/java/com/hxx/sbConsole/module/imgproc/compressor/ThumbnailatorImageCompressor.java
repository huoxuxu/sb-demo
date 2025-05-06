package com.hxx.sbConsole.module.imgproc.compressor;

import com.hxx.sbConsole.commons.util.ThumbnailsUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Slf4j
@var
public class ThumbnailatorImageCompressor {

    public static void main(String[] args) {
        try {
            var path = "D:\\tmp\\zt-dev\\risk\\pic.jpg";
            File file = new File(path);
            byte[] imgBytes = FileUtils.readFileToByteArray(file);
            // 压缩 768*432
            byte[] compessImgBytes = ThumbnailsUtil.compressImg(imgBytes, 768, 432, 0.5f);
            log.info("img压缩：before：{} after：{}", imgBytes.length, compessImgBytes.length);
            if (compessImgBytes.length > imgBytes.length) {
                compessImgBytes = imgBytes;
            }
            if (compessImgBytes.length < 1) {
                log.info("压缩后图片为空！");
                return;
            }

            File targetFile = new File(path + ".jpg");
            FileUtils.writeByteArrayToFile(targetFile, compessImgBytes);

        } catch (Exception ex) {
            System.out.println(ExceptionUtils.getStackTrace(ex));
        }
    }

    public static void case1() {
        var imgDirPath = "D:\\tmp\\zt-dev\\risk\\img";
        File imgDir = new File(imgDirPath);
        var files = imgDir.listFiles(File::isFile);


        int targetWidth = 800;
        int targetHeight = 600;
        float imageQuality = 0.5f;

        for (File file : files) {
            var inputImagePath = file.getPath();
            try {

                var outputImagePath = inputImagePath + "-compress.jpg";
                compressToJpeg(inputImagePath, outputImagePath, targetWidth, targetHeight, imageQuality);

                System.out.println("图片压缩成功！" + inputImagePath);
            } catch (Exception e) {
                System.out.println("图片压缩失败：" + e.getMessage());
            }
        }
    }

    public static void compressImage(String inputImagePath,
                                     String outputImagePath,
                                     int targetWidth,
                                     int targetHeight,
                                     float imageQuality) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage originalImage = ImageIO.read(inputFile);
        System.out.println("开始处理：" + inputImagePath + " W:" + originalImage.getWidth() + " H:" + originalImage.getHeight());

        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
//                .size(originalImage.getWidth(), originalImage.getHeight())
                .outputQuality(imageQuality)
                .toFile(new File(outputImagePath));
    }

    public static void compressToJpeg(String inputImagePath,
                                      String outputImagePath,
                                      int targetWidth,
                                      int targetHeight,
                                      float imageQuality) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        Thumbnails.of(originalImage)
//                .size(targetWidth, targetHeight)
                .size(originalImage.getWidth(), originalImage.getHeight())
                .outputFormat("jpg") // 指定输出格式为 JPEG
                .outputQuality(imageQuality)
                .toFile(new File(outputImagePath));
    }
}
