package com.hxx.sbConsole.commons.util;

import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class WebpUtil {

    /**
     * jpg转webp 字节数组
     *
     * @param imgFile
     * @param compressionQuality
     * @return
     * @throws Exception
     */
    public static byte[] toWebpBytes(File imgFile, float compressionQuality)
            throws Exception {
        ByteArrayOutputStream imgOutStream = new ByteArrayOutputStream();
        toWebp(imgFile, compressionQuality, new MemoryCacheImageOutputStream(imgOutStream));
        return imgOutStream.toByteArray();
    }

    /**
     * jpg转webp
     *
     * @param imgFile            原图片
     * @param compressionQuality 图像质量. 设置范围 0-1
     * @param webpOutputStream   写入webp的流
     * @throws Exception
     */
    public static void toWebp(File imgFile, float compressionQuality, ImageOutputStream webpOutputStream)
            throws Exception {
        BufferedImage image = ImageIO.read(imgFile);
        toWebp(image, compressionQuality, webpOutputStream);
    }

    /**
     * jpg转webp
     *
     * @param imgFileBytes
     * @param compressionQuality
     * @param webpOutputStream
     * @throws Exception
     */
    public static void toWebp(byte[] imgFileBytes, float compressionQuality, ImageOutputStream webpOutputStream)
            throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(imgFileBytes);
        BufferedImage image = ImageIO.read(bis);
        toWebp(image, compressionQuality, webpOutputStream);
    }

    /**
     * jpg转webp
     *
     * @param image              原图片
     * @param compressionQuality 图像质量. 设置范围 0-1
     * @param webpOutputStream   写入webp的流
     * @throws Exception
     */
    public static void toWebp(BufferedImage image, float compressionQuality, ImageOutputStream webpOutputStream)
            throws Exception {
        ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
        try {
            // 设置保存参数
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // 设置有损压缩
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSY_COMPRESSION]);
            //设置 80% 的质量. 设置范围 0-1
            writeParam.setCompressionQuality(compressionQuality);

            // Save the image
            writer.setOutput(webpOutputStream);
            IIOImage img = new IIOImage(image, null, null);
            writer.write(null, img, writeParam);
        } finally {
            writer.dispose();
        }
    }

}
