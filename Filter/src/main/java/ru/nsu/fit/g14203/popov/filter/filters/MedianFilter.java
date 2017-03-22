package ru.nsu.fit.g14203.popov.filter.filters;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MedianFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = Util.copyImage(image);

        int[][] window = new int[5][5];
        int length = window.length * window.length / 2 + 1;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Util.fillArea(window, x, y, image);
                int R = Arrays.stream(window)
                        .flatMapToInt(arr -> Arrays.stream(arr).map(i -> (i & 0xFF0000)))
                        .sorted()
                        .limit(length)
                        .skip(length - 1)
                        .findFirst()
                        .orElse(0);
                int G = Arrays.stream(window)
                        .flatMapToInt(arr -> Arrays.stream(arr).map(i -> (i & 0x00FF00)))
                        .sorted()
                        .limit(length)
                        .skip(length - 1)
                        .findFirst()
                        .orElse(0);
                int B = Arrays.stream(window)
                        .flatMapToInt(arr -> Arrays.stream(arr).map(i -> (i & 0x0000FF)))
                        .sorted()
                        .limit(length)
                        .skip(length - 1)
                        .findFirst()
                        .orElse(0);

                result.setRGB(x, y, R + G + B);
            }
        }

        return result;
    }
}
