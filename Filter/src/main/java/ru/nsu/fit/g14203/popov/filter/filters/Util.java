package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;

public class Util {

    static void fillArea(int[][] matrix, int x, int y, BufferedImage image) {
        int size = matrix.length / 2;

        for (int dx = -size; dx <= size; dx++) {
            for (int dy = -size; dy <= size; dy++) {
                int __x = (x + dx < 0) ? 0
                                       : (x + dx >= image.getWidth()) ? image.getWidth() - 1
                                                                      : x + dx;
                int __y = (y + dy < 0) ? 0
                                       : (y + dy >= image.getHeight()) ? image.getHeight() - 1
                                                                       : y + dy;
                matrix[dx + size][dy + size] = image.getRGB(__x, __y);
            }
        }
    }

    static int getColorValue(int RGB) {
        int R = (RGB & 0xFF0000) / 0x010000;
        int G = (RGB & 0x00FF00) / 0x000100;
        int B = (RGB & 0x0000FF);
        return (int) (R * 0.299 + G * 0.587 + B * 0.114 + 0.5);
    }

    static int getClosestColor(int C, int paletteSize) {
        double d = ((double) 0xFF) / (paletteSize - 1);     //  black, white - 1 orb
        if (C <= d / 2)
            return 0x00;
        if (C > 0xFF - (d / 2))
            return 0xFF;

        int orb = (int) ((C - (d / 2)) / d);                //  with no rounding
        return (0xFF / (paletteSize - 1)) * (orb + 1);
    }

    public static BufferedImage copyImage(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++)
                result.setRGB(x, y, image.getRGB(x, y));
        }

        return result;
    }
}
