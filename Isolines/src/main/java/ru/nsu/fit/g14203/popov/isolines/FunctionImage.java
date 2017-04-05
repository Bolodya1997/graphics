package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.ReversedBufferedImage;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

class FunctionImage extends ReversedBufferedImage {

    FunctionImage(int width, int height,
                  Point2D.Double from, double dx, double dy,
                  Function2D function, Legend legend) {
        super(width, height, TYPE_INT_RGB);

        int __x = 0;
        for (double x = from.getX(); __x < getWidth(); x += dx, __x++) {

            int __y = 0;
            for (double y = from.getY(); __y < getHeight(); y += dy, __y++) {
                int i = Arrays.binarySearch(legend.getBorders(), function.getValue(x, y));
                i = (i < 0) ? -(i + 1) : i;

                setRGB(__x, __y, legend.getColors()[i].getRGB());
            }
        }
    }
}
