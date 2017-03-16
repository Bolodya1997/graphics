package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

class Util {

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

    static void fillWindow(int[][] window, int x, int y, BufferedImage image) {
        int size = window.length;

        for (int dx = 0; dx < size; dx++) {
            for (int dy = 0; dy < size; dy++) {
                int __x = (x + dx < 0) ? 0
                        : (x + dx >= image.getWidth()) ? image.getWidth() - 1
                        : x + dx;
                int __y = (y + dy < 0) ? 0
                        : (y + dy >= image.getHeight()) ? image.getHeight() - 1
                        : y + dy;
                window[dx][dy] = image.getRGB(__x, __y);
            }
        }
    }

    static int getColorValue(int RGB) {
        int R = (RGB & 0xFF0000) / 0x010000;
        int G = (RGB & 0x00FF00) / 0x000100;
        int B = (RGB & 0x0000FF);
        return (int) (R * 0.299 + G * 0.587 + B * 0.114 + 0.5);
    }
}
