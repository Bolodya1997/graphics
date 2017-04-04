package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;

class FunctionImage extends BufferedImage {

    FunctionImage(int width, int height,
                  Point2D.Double from, Point2D.Double to, double dx, double dy,
                  Function2D function, Legend legend) {
        super(width, height, TYPE_INT_RGB);

        int __x = 0;
        for (double x = from.getX(); x < to.getX(); x += dx, __x++) {

            int __y = 0;
            for (double y = from.getY(); y < to.getY(); y += dy, __y++) {
                int i = Arrays.binarySearch(legend.getBorders(), function.getValue(x, y));
                i = (i < 0) ? -(i + 1) : i;

                setRGB(__x, __y, legend.getColors()[i].getRGB());
            }
        }
    }
}
