package com.hxx.sbConsole.commons.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailsUtil {


    public static byte[] compressImg(byte[] inputBytes, int width, int height, float quality)
            throws IOException {
        // 将 byte[] 转换为 ByteArrayInputStream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        // 获取图片的原始宽度和高度
        BufferedImage originalImage = ImageIO.read(inputStream);
        if (originalImage == null) {
            throw new IOException("无法读取图片");
        }
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // 如果原始宽高小于目标宽高
        if (originalWidth < width && originalHeight < height) {
            width = originalWidth;
            height = originalHeight;
        }

        // 重新创建输入流，因为上面的 read 方法已经消耗了流
        inputStream = new ByteArrayInputStream(inputBytes);

        // 创建 ByteArrayOutputStream 用于存储压缩后的字节数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 使用 Thumbnails 进行压缩
        Thumbnails.of(inputStream)
                .size(width, height)
                .outputQuality(quality)
                .outputFormat("jpg")
                .toOutputStream(outputStream);

        // 获取压缩后的字节数组
        return outputStream.toByteArray();
    }


    /**
     * 合并图片，每组n个
     *
     * @param groupSize 一组的图片个数
     * @param images
     * @return
     * @throws IOException
     */
    public static List<BufferedImage> combineImages(int groupSize, List<BufferedImage> images) throws IOException {
        List<BufferedImage> vos = new ArrayList<>();
        for (int i = 0; i < images.size(); i += groupSize) {
            List<BufferedImage> group = images.subList(i, Math.min(i + groupSize, images.size()));
            if (group.size() < 6) {
                while (group.size() < 6) {
                    group.add(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                }
            }
            BufferedImage combinedImage = combineGroup(group);
            vos.add(combinedImage);
        }
        return vos;
    }

    private static BufferedImage combineGroup(List<BufferedImage> group) throws IOException {
        int maxWidth = 0;
        int totalHeight = 0;
        int halfGroupSize = group.size() / 2;

        for (int i = 0; i < halfGroupSize; i++) {
            maxWidth = Math.max(maxWidth, group.get(i).getWidth());
            totalHeight = Math.max(totalHeight, group.get(i).getHeight());
        }
        maxWidth *= 3;
        totalHeight *= 2;

        BufferedImage combined = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);

        int x = 0;
        int y = 0;
        int imageWidth = maxWidth / 3;
        int imageHeight = totalHeight / 2;

        for (int i = 0; i < halfGroupSize; i++) {
            BufferedImage resized = Thumbnails.of(group.get(i))
                    .size(imageWidth, imageHeight)
                    .asBufferedImage();
            combined.getGraphics().drawImage(resized, x, y, null);
            x += imageWidth;
        }

        x = 0;
        y = totalHeight / 2;
        for (int i = halfGroupSize; i < group.size(); i++) {
            BufferedImage resized = Thumbnails.of(group.get(i))
                    .size(imageWidth, imageHeight)
                    .asBufferedImage();
            combined.getGraphics().drawImage(resized, x, y, null);
            x += imageWidth;
        }

        return combined;
    }

    public static void convertImg() throws IOException {
        //outputFormat(图像格式)
        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .outputFormat("png")
                .toFile("c:/a380_1280x1024.png");

        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .outputFormat("gif")
                .toFile("c:/a380_1280x1024.gif");
    }

    public static void outputStream() throws IOException {
        //toOutputStream(流对象)
        OutputStream os = new FileOutputStream("c:/a380_1280x1024_OutputStream.png");
        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .toOutputStream(os);

        //asBufferedImage()返回BufferedImage
        BufferedImage thumbnail = Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File("c:/a380_1280x1024_BufferedImage.jpg"));
    }

    /**
     * 缩放图片
     * 若图片横比200小，高比300小，不变
     * 若图片横比200小，高比300大，高缩小到300，图片比例不变
     * 若图片横比200大，高比300小，横缩小到200，图片比例不变
     * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
     *
     * @param imgPaths
     * @param width
     * @param height
     * @throws IOException
     */
    public static void procImg(List<String> imgPaths, int width, int height) throws IOException {
        Thumbnails.of(imgPaths.toArray(new String[0]))
                //size(宽度, 高度)
                .size(width, height)
                .toFile("c:/a380_200x300.jpg");

        //scale(比例)
        Thumbnails.of("images/a380_1280x1024.jpg")
                .scale(0.25f)
                .toFile("c:/a380_25%.jpg");

        Thumbnails.of("images/a380_1280x1024.jpg")
                .scale(1.10f)
                .toFile("c:/a380_110%.jpg");

        //rotate(角度),正数：顺时针负数：逆时针
        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .rotate(90)
                .toFile("c:/a380_rotate+90.jpg");

        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .rotate(-90)
                .toFile("c:/a380_rotate-90.jpg");

        //keepAspectRatio(false)默认是按照比例缩放的
        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile("c:/a380_200x200.jpg");

    }

    public static void waterMark() throws IOException {
        //watermark(位置，水印图，透明度)
        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f)
                .toFile("c:/a380_watermark_bottom_right.jpg");

        Thumbnails.of("images/a380_1280x1024.jpg")
                .size(1280, 1024)
                .watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f)
                .outputQuality(0.8f)
                .toFile("c:/a380_watermark_center.jpg");
    }

    public static void cutImg() throws IOException {
        //sourceRegion()

//图片中心400*400的区域
        Thumbnails.of("images/a380_1280x1024.jpg")
                .sourceRegion(Positions.CENTER, 400, 400)
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile("c:/a380_region_center.jpg");

//图片右下400*400的区域
        Thumbnails.of("images/a380_1280x1024.jpg")
                .sourceRegion(Positions.BOTTOM_RIGHT, 400, 400)
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile("c:/a380_region_bootom_right.jpg");

//指定坐标
        Thumbnails.of("images/a380_1280x1024.jpg")
                .sourceRegion(600, 500, 400, 400)
                .size(200, 200)
                .keepAspectRatio(false)
                .toFile("c:/a380_region_coord.jpg");
    }
}
