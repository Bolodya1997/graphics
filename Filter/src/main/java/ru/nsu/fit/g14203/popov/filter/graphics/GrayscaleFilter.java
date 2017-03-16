package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class GrayscaleFilter implements Filter {
    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int gray = Util.getColorValue(image.getRGB(x, y));

                int RGB = gray * 0x010000
                        + gray * 0x000100
                        + gray;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }
}
