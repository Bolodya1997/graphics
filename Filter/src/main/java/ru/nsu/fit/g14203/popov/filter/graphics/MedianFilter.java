package ru.nsu.fit.g14203.popov.filter.graphics;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MedianFilter implements Filter {

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[][] window = new int[5][5];
        int length = window.length * window.length / 2 + 1;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Util.fillArea(window, x, y, image);
                int R = Arrays.stream(window)
                        .flatMap(arr -> Arrays.stream(arr).mapToObj(i -> (i & 0xFF0000)))
                        .sorted(Integer::compareTo)
                        .limit(length)
                        .skip(length - 1)
                        .findFirst()
                        .orElse(0);
                int G = Arrays.stream(window)
                        .flatMap(arr -> Arrays.stream(arr).mapToObj(i -> (i & 0x00FF00)))
                        .sorted(Integer::compareTo)
                        .limit(length)
                        .skip(length - 1)
                        .findFirst()
                        .orElse(0);
                int B = Arrays.stream(window)
                        .flatMap(arr -> Arrays.stream(arr).mapToObj(i -> (i & 0x0000FF)))
                        .sorted(Integer::compareTo)
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
