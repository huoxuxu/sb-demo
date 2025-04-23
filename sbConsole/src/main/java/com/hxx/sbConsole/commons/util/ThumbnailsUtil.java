package com.hxx.sbConsole.commons.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ThumbnailsUtil {

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
