package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class InvertFilter implements SimpleFilter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int RGB = image.getRGB(x, y) & 0xFFFFFF;
                result.setRGB(x, y, 0xFFFFFF - RGB);
            }
        }

        return result;
    }
}
