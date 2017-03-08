package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class MagnifyFilter implements SimpleFilter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[][][] place = { { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
                            { { 1, 0 }, { 2, 0 }, { 1, 1 }, { 2, 1 } },
                            { { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 } },
                            { { 1, 1 }, { 2, 1 }, { 1, 2 }, { 2, 2 } } };
        int[][] res = { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } };

        int[][] window = new int[3][3];
        for (int x = 0; x < image.getWidth() / 2; x++) {
            for (int y = 0; y < image.getHeight() / 2; y++) {

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int __x = x + dx + image.getWidth() / 4;
                        int __y = y + dy + image.getHeight() / 4;
                        window[dx + 1][dy + 1] = image.getRGB(__x, __y);
                    }
                }

                for (int i = 0; i < 4; i++) {
                    int R = 0;
                    int G = 0;
                    int B = 0;
                    for (int k = 0; k < 4; k++) {
                        R += (window[place[i][k][0]][place[i][k][1]] & 0xFF0000) / 0x010000;
                        G += (window[place[i][k][0]][place[i][k][1]] & 0x00FF00) / 0x000100;
                        B += (window[place[i][k][0]][place[i][k][1]] & 0x0000FF);
                    }

                    int RGB = ((R / 4) & 0xFF) * 0x010000
                            + ((G / 4) & 0xFF) * 0x000100
                            + ((B / 4) & 0xFF);
                    result.setRGB(x * 2 + res[i][0], y * 2 + res[i][1], RGB);
                }
            }
        }

        return result;
    }
}
