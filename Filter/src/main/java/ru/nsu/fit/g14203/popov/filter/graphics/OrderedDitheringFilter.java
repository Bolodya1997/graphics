package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class OrderedDitheringFilter implements Filter {

    private static final int[][] THRESHOLD = { {  0, 48, 12, 60,  3, 51, 15, 63 },
                                               { 32, 16, 44, 28, 35, 19, 47, 31 },
                                               {  8, 56,  4, 52, 11, 59,  7, 55 },
                                               { 40, 24, 36, 20, 43, 27, 39, 23 },
                                               {  2, 50, 14, 62,  1, 49, 13, 61 },
                                               { 34, 18, 46, 30, 33, 17, 45, 29 },
                                               { 10, 58,  6, 54,  9, 57,  5, 53 },
                                               { 42, 26, 38, 22, 41, 25, 37, 21 } };

    private int RSize = 8;
    private int GSize = 8;
    private int BSize = 8;

    public void setRSize(int RSize) {
        this.RSize = RSize;
    }

    public void setGSize(int GSize) {
        this.GSize = GSize;
    }

    public void setBSize(int BSize) {
        this.BSize = BSize;
    }

    private double getThreshold(int x, int y) {
        return THRESHOLD[x % 8][y % 8] / 64.0;
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int R = (image.getRGB(x, y) & 0xFF0000) / 0x010000;
                int G = (image.getRGB(x, y) & 0x00FF00) / 0x000100;
                int B = (image.getRGB(x, y) & 0x0000FF);

                R = (int) (R + 0xFF * (getThreshold(x, y) - 0.5) + 0.5);
                R = (R < 0) ? 0
                        : (R > 0xFF) ? 0xFF
                        : R;
                G = (int) (G + 0xFF * (getThreshold(x, y) - 0.5) + 0.5);
                G = (G < 0) ? 0
                        : (G > 0xFF) ? 0xFF
                        : G;
                B = (int) (B + 0xFF * (getThreshold(x, y) - 0.5) + 0.5);
                B = (B < 0) ? 0
                        : (B > 0xFF) ? 0xFF
                        : B;

                R = R / (0xFF / RSize) * (0xFF / RSize);
                G = G / (0xFF / GSize) * (0xFF / GSize);
                B = B / (0xFF / BSize) * (0xFF / BSize);

                int RGB = R * 0x010000
                        + G * 0x000100
                        + B;
                result.setRGB(x, y, RGB);
            }
        }
        return result;
    }
}
