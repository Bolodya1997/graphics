package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

public class MagnifyFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = Util.copyImage(image);
        for (int x = 0; x < image.getWidth() / 2; x++) {
            for (int y = 0; y < image.getHeight() / 2; y++) {
                int RGB = image.getRGB(x + image.getWidth() / 4, y + image.getHeight() / 4);
                result.setRGB(x * 2, y * 2, RGB);
                result.setRGB(x * 2 + 1, y * 2, RGB);
                result.setRGB(x * 2, y * 2 + 1, RGB);
                result.setRGB(x * 2 + 1, y * 2 + 1, RGB);
            }
        }

        return result;
    }
}
