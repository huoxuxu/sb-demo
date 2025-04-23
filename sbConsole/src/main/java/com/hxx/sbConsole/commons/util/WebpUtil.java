package com.hxx.sbConsole.commons.util;

import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebpUtil {

    /**
     * jpg转webp
     *
     * @param imgFile            原图片
     * @param compressionQuality 图像质量. 设置范围 0-1
     * @param webpStream         写入webp的流
     * @throws Exception
     */
    public static void toWebp(File imgFile, float compressionQuality, ImageOutputStream webpStream)
            throws Exception {
        BufferedImage image = ImageIO.read(imgFile);
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
            writer.setOutput(webpStream);
            IIOImage img = new IIOImage(image, null, null);
            writer.write(null, img, writeParam);
        } finally {
            writer.dispose();
        }
    }


}
