package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

abstract class MatrixFilter implements SimpleFilter {

    abstract int[][] getK();
    abstract int getW();

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[][] K = getK();
        int W = getW();

        int[][] window = new int[3][3];
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {

                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int __x = (x + dx < 0) ? 0
                                : (x + dx >= image.getWidth()) ? image.getWidth() - 1
                                : x + dx;
                        int __y = (y + dy < 0) ? 0
                                : (y + dy >= image.getHeight()) ? image.getHeight() - 1
                                : y + dy;
                        window[dx + 1][dy + 1] = image.getRGB(__x, __y);
                    }
                }

                int R = 0;
                int G = 0;
                int B = 0;
                for (int i = 0; i < 3; i++) {
                    for (int k = 0; k < 3; k++) {
                        R += (window[i][k] & 0xFF0000) / 0x010000 * K[i][k];
                        G += (window[i][k] & 0x00FF00) / 0x000100 * K[i][k];
                        B += (window[i][k] & 0x0000FF) * K[i][k];
                    }
                }

                R /= W;
                R = (R < 0) ? 0
                            : (R > 0xFF) ? 0xFF
                                         : R;
                G /= W;
                G = (G < 0) ? 0
                            : (G > 0xFF) ? 0xFF
                                         : G;
                B /= W;
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
