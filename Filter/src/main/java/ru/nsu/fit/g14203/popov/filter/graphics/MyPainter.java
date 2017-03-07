package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;

public class MyPainter {

    /**
     * Proportional shrink image to fit {@param width} x {@param height} rectangle.
     *
     * @param image             base image
     * @param width             new maximum width
     * @param height            new maximum height
     * @return                  shrunk image
     */
    public static BufferedImage shrinkImage(BufferedImage image, int width, int height) {
        if (image.getWidth() < width || image.getHeight() < height)
            return image;

        int[][][] colors;   //  [x][y]{ R, G, B, count }
        BufferedImage result;
        if ((double) image.getWidth() / width > (double) image.getHeight() / height) {
            colors = countXColors(image, width, height);
            result = new BufferedImage(width, image.getHeight() * width / image.getWidth(),
                    BufferedImage.TYPE_INT_RGB);
        } else {
            colors = countYColors(image, width, height);
            result = new BufferedImage(image.getWidth() * height / image.getHeight(), height,
                    BufferedImage.TYPE_INT_RGB);
        }

        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                int count = colors[x][y][3];
                if (count == 0)
                    continue;

                int RGB = colors[x][y][0] / count * 0x010000
                        + colors[x][y][1] / count * 0x000100
                        + colors[x][y][2] / count;
                result.setRGB(x, y, RGB);
            }
        }

        return result;
    }

    private static int[][][] countXColors(BufferedImage image, int width, int height) {
        int[][][] colors = new int[width][height][4];

        for (int oldX = 0; oldX < image.getWidth(); oldX++) {
            for (int oldY = 0; oldY < image.getHeight(); oldY++) {
                int x = oldX * width / image.getWidth();
                int y = Integer.min(oldY * width / image.getWidth(), height - 1);
                colors[x][y][0] += (image.getRGB(oldX, oldY) & 0xFF0000) / 0x010000;
                colors[x][y][1] += (image.getRGB(oldX, oldY) & 0x00FF00) / 0x000100;
                colors[x][y][2] += (image.getRGB(oldX, oldY) & 0x0000FF);
                colors[x][y][3] += 1;
            }
        }

        return colors;
    }

    private static int[][][] countYColors(BufferedImage image, int width, int height) {
        int[][][] colors = new int[width][height][4];

        for (int oldX = 0; oldX < image.getWidth(); oldX++) {
            for (int oldY = 0; oldY < image.getHeight(); oldY++) {
                int x = Integer.min(oldX * height / image.getHeight(), width - 1);
                int y = oldY * height / image.getHeight();
                colors[x][y][0] += (image.getRGB(oldX, oldY) & 0xFF0000) / 0x010000;
                colors[x][y][1] += (image.getRGB(oldX, oldY) & 0x00FF00) / 0x000100;
                colors[x][y][2] += (image.getRGB(oldX, oldY) & 0x0000FF);
                colors[x][y][3] += 1;
            }
        }

        return colors;
    }
}
