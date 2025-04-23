package com.hxx.sbConsole.module.imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.var;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

@var
public class ImageCombiner {
    public static void main(String[] args) {
        // 假设图片都在这个目录下
        String imageDirectory = "path/to/your/images";
        try {
            File directory = new File(imageDirectory);
            var imageFiles = directory.listFiles((dir, name) ->
                    name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

            int groupSize = 6;
            // 合并
            var combImgs = combineImages(imageFiles, groupSize);

            for (int i = 0; i < combImgs.size(); i++) {
                BufferedImage combImg = combImgs.get(i);

                File outputFile = new File(imageDirectory + "/combined_" + i + ".jpg");
                ImageIO.write(combImg, "jpg", outputFile);
            }
        } catch (Exception e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
    }

    public static List<BufferedImage> combineImages(File[] imageFiles, int groupSize)
            throws IOException {
        if (ArrayUtils.isEmpty(imageFiles)) {
            System.out.println("未找到图片文件。");
            return new ArrayList<>();
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File imageFile : imageFiles) {
            images.add(ImageIO.read(imageFile));
        }

        List<BufferedImage> combImgs = new ArrayList<>();
        for (int i = 0; i < images.size(); i += groupSize) {
            List<BufferedImage> group = images.subList(i, Math.min(i + groupSize, images.size()));
            if (group.size() < 6) {
                // 不足 6 张图片，补齐空白图片
                while (group.size() < 6) {
                    group.add(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                }
            }
            BufferedImage combinedImage = combineGroup(group);
            combImgs.add(combinedImage);
        }
        return combImgs;
    }

    private static BufferedImage combineGroup(List<BufferedImage> group) {
        int maxWidth = 0;
        int totalHeight = 0;
        int halfGroupSize = group.size() / 2;

        // 计算每行的最大宽度和总高度
        for (int i = 0; i < halfGroupSize; i++) {
            maxWidth = Math.max(maxWidth, group.get(i).getWidth());
            totalHeight = Math.max(totalHeight, group.get(i).getHeight());
        }
        maxWidth *= 3;
        totalHeight *= 2;

        BufferedImage combined = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = combined.createGraphics();

        int x = 0;
        int y = 0;
        int imageWidth = maxWidth / 3;
        int imageHeight = totalHeight / 2;

        // 绘制上部的 3 张图片
        for (int i = 0; i < halfGroupSize; i++) {
            BufferedImage image = group.get(i);
            g2d.drawImage(image, x, y, imageWidth, imageHeight, null);
            x += imageWidth;
        }

        x = 0;
        y = totalHeight / 2;
        // 绘制下部的 3 张图片
        for (int i = halfGroupSize; i < group.size(); i++) {
            BufferedImage image = group.get(i);
            g2d.drawImage(image, x, y, imageWidth, imageHeight, null);
            x += imageWidth;
        }

        g2d.dispose();
        return combined;
    }
}
