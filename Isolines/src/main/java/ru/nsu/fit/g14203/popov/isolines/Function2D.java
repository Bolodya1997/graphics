package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;

abstract class Function2D {

    abstract double getValue(double x, double y);

    double[] getMinMax(Point2D.Double from, Point2D.Double to, double dx, double dy) {
        double[] result = { Double.MAX_VALUE, Double.MIN_VALUE };   //  [min, max]
        for (double x = from.getX(); x < to.getX(); x += dx) {
            for (double y = from.getY(); y < to.getY(); y += dy) {
                double value = getValue(x, y);
                result[0] = Math.min(value, result[0]);
                result[1] = Math.max(value, result[1]);
            }
        }

        return result;
    }
}
