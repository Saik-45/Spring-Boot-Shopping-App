package com.sai.Product.ImageSize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {

    // Method to resize and store image
    public static byte[] resizeAndStoreImage(byte[] originalImageBytes) throws IOException {
        // Read original image bytes
        ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
        BufferedImage originalImage = ImageIO.read(bis);
        bis.close();

        // Resize image to 240px height and 180px width
        int newHeight = 240;
        int newWidth = 180;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        // Convert resized image to bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", bos);
        bos.close();

        return bos.toByteArray();
    }




        // Method to retrieve image with specific dimensions
        public static byte[] retrieveImageWithSpecificDimensions(byte[] originalImageBytes, int desiredWidth, int desiredHeight) throws IOException {
            // Read original image bytes
            ByteArrayInputStream bis = new ByteArrayInputStream(originalImageBytes);
            BufferedImage originalImage = ImageIO.read(bis);
            bis.close();

            // Resize image to desired dimensions
            BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, desiredWidth, desiredHeight, null);
            g.dispose();

            // Convert resized image to bytes
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", bos);
            bos.close();

            return bos.toByteArray();
        }
    }

