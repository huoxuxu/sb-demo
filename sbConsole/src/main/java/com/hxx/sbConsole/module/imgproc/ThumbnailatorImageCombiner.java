package com.hxx.sbConsole.module.imgproc;

import lombok.var;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailatorImageCombiner {

    public static void main(String[] args) {
        var imgDirPath = "D:\\tmp\\zt-dev\\risk\\img";
        try {
            combineImages(imgDirPath);
        } catch (IOException e) {
            System.out.println(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("ok!");
    }

    public static void combineImages(String imageDirectory) throws IOException {
        File directory = new File(imageDirectory);
        File[] imageFiles = directory.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));
        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("未找到图片文件。");
            return;
        }

        List<BufferedImage> images = new ArrayList<>();
        for (File imageFile : imageFiles) {
            images.add(ImageIO.read(imageFile));
        }

        int groupSize = 6;
        for (int i = 0; i < images.size(); i += groupSize) {
            List<BufferedImage> group = images.subList(i, Math.min(i + groupSize, images.size()));
            if (group.size() < 6) {
                while (group.size() < 6) {
                    group.add(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
                }
            }
            BufferedImage combinedImage = combineGroup(group);
            File outputFile = new File(imageDirectory + "/combined_" + (i / groupSize) + ".jpg");
            ImageIO.write(combinedImage, "jpg", outputFile);
        }
    }

    /**
     * 合并图片，每组n个
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
}
