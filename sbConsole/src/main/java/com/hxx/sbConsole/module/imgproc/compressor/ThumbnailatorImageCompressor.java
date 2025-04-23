package com.hxx.sbConsole.module.imgproc.compressor;

import lombok.var;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@var
public class ThumbnailatorImageCompressor {

    public static void main(String[] args) {
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

//        String inputImagePath = "path/to/your/input/image.jpg";
//        String outputImagePath = "path/to/your/output/image.jpg";
//
//        try {
//            compressImage(inputImagePath, outputImagePath, targetWidth, targetHeight, imageQuality);
//            System.out.println("图片压缩成功！");
//        } catch (IOException e) {
//            System.out.println("图片压缩失败：" + e.getMessage());
//        }
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
