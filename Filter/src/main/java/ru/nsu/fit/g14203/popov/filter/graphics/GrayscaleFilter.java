package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class GrayscaleFilter implements SimpleFilter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int R = (image.getRGB(x, y) & 0xFF0000) / 0x010000;
                int G = (image.getRGB(x, y) & 0x00FF00) / 0x000100;
                int B = (image.getRGB(x, y) & 0x0000FF);
                int gray = (int) (R * 0.299 + G * 0.587 + B * 0.114 + 0.5);

                int RGB = gray * 0x010000
                        + gray * 0x000100
                        + gray;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }
}
