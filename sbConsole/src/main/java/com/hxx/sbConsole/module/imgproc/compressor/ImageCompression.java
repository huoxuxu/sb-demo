package com.hxx.sbConsole.module.imgproc.compressor;

import lombok.var;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@var
public class ImageCompression {

    public static void main(String[] args) {
        var imgDirPath = "D:\\tmp\\zt-dev\\risk\\img";
        File imgDir = new File(imgDirPath);
        var files = imgDir.listFiles(File::isFile);

        int targetWidth = 800;
        int targetHeight = 600;
        float imageQuality = 0.1f; // 图像质量，范围 0.0f - 1.0f

        for (File file : files) {
            var inputImagePath= file.getPath();
            var outputImagePath = inputImagePath + "-compress.png";
            try {
                // 压缩
                compressImage(inputImagePath, outputImagePath, targetWidth, targetHeight, imageQuality);

                System.out.println("图片压缩成功！");
            } catch (IOException e) {
                System.out.println("图片压缩失败：" + e.getMessage());
            }
        }

//        String inputImagePath = "path/to/your/input/image.jpg";
//        String outputImagePath = "path/to/your/output/image.jpg";
//
//        try {
//            // 压缩
//            compressImage(inputImagePath, outputImagePath, targetWidth, targetHeight, imageQuality);
//
//            System.out.println("图片压缩成功！");
//        } catch (IOException e) {
//            System.out.println("图片压缩失败：" + e.getMessage());
//        }
    }

    /**
     * 压缩图片
     *
     * @param inputImagePath
     * @param outputImagePath
     * @param targetWidth
     * @param targetHeight
     * @param imageQuality
     * @throws IOException
     */
    public static void compressImage(String inputImagePath,
                                     String outputImagePath,
                                     int targetWidth,
                                     int targetHeight,
                                     float imageQuality) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage originalImage = ImageIO.read(inputFile);

        // 创建一个新的 BufferedImage 对象，指定目标宽度和高度
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();

        try {
            // 设置抗锯齿和图像质量
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        } finally {
            g2d.dispose();
        }

        // 获取输出文件的扩展名
        String fileExtension = getFileExtension(outputImagePath);

        // 获取合适的 ImageWriter
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(fileExtension);
        if (!writers.hasNext()) {
            throw new IllegalStateException("未找到合适的 ImageWriter 用于 " + fileExtension + " 格式");
        }
        ImageWriter writer = writers.next();

        // 获取 ImageWriteParam 并设置图像质量
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(imageQuality);
        }

        // 创建输出流并写入图像
        try (FileImageOutputStream outputStream = new FileImageOutputStream(new File(outputImagePath))) {
            writer.setOutput(outputStream);
            IIOImage iioImage = new IIOImage(resizedImage, null, null);
            writer.write(null, iioImage, param);
        } finally {
            writer.dispose();
        }
    }

    private static String getFileExtension(String filePath) {
        int lastIndex = filePath.lastIndexOf(".");
        if (lastIndex != -1) {
            return filePath.substring(lastIndex + 1);
        }
        return "jpg";
    }
}
