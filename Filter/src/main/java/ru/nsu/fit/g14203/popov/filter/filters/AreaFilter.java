package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

abstract class AreaFilter implements Filter {

    abstract int[][] getKernel();
    abstract int getDivider();
    abstract int getOffset();

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = Util.copyImage(image);

        int[][] kernel = getKernel();
        int divider = getDivider();
        int offset = getOffset();

        int[][] area = new int[3][3];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Util.fillArea(area, x, y, image);

                int R = 0;
                int G = 0;
                int B = 0;
                for (int i = 0; i < 3; i++) {
                    for (int k = 0; k < 3; k++) {
                        R += (area[i][k] & 0xFF0000) / 0x010000 * kernel[i][k];
                        G += (area[i][k] & 0x00FF00) / 0x000100 * kernel[i][k];
                        B += (area[i][k] & 0x0000FF) * kernel[i][k];
                    }
                }

                R = R / divider + offset;
                R = (R < 0) ? 0
                            : (R > 0xFF) ? 0xFF
                                         : R;
                G = G / divider + offset;
                G = (G < 0) ? 0
                            : (G > 0xFF) ? 0xFF
                                         : G;
                B = B / divider + offset;
                B = (B < 0) ? 0
                            : (B > 0xFF) ? 0xFF
                                         : B;

                int RGB = R * 0x010000
                        + G * 0x000100
                        + B;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }
}
