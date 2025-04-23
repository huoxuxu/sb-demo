package com.hxx.sbConsole.module.webp;

import com.hxx.sbConsole.commons.util.WebpUtil;
import com.luciad.imageio.webp.WebPWriteParam;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WebpServiceDemo {
    public static void main(String[] args) {

    }


    /**
     * 无损压缩
     */
    private static void convertJpegToWebpLossless() {
        try {
            BufferedImage image = ImageIO.read(new File("C:\\Users\\admin\\Desktop\\pbd00106jp-7.jpg"));
            ImageWriter writer = ImageIO.getImageWritersByMIMEType("image/webp").next();
            WebPWriteParam writeParam = new WebPWriteParam(writer.getLocale());
            writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            // 设置无损
            writeParam.setCompressionType(writeParam.getCompressionTypes()[WebPWriteParam.LOSSLESS_COMPRESSION]);

            // 保存图片
            writer.setOutput(new FileImageOutputStream(new File("C:\\Users\\admin\\Desktop\\sample.webp")));
            writer.write(null, new IIOImage(image, null, null), writeParam);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void convertJpegToWebpWithLossyCompression()
            throws Exception {
        File jpgFile = new File("C:\\Users\\admin\\Desktop\\pbd00106jp-7.jpg");
        File webpFile = new File("C:\\Users\\admin\\Desktop\\sample.webp");

        try (FileImageOutputStream webpStream = new FileImageOutputStream(webpFile)) {
            WebpUtil.toWebp(jpgFile, 0.8f, webpStream);
        }
    }
}
