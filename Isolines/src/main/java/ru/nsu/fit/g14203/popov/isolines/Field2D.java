package ru.nsu.fit.g14203.popov.isolines;

import java.util.ArrayList;
import java.util.List;

class Field2D {

    private class Charge {
        double x;
        double y;
        double q;

        Charge(double x, double y, double q) {
            this.x = x;
            this.y = y;
            this.q = q;
        }

        double influenceOn(double __x, double __y) {
            double r = Math.sqrt( (x - __x) * (x - __x) +
                                  (y - __y) * (y - __y) );

            return q * (Math.max(Math.abs(q) - r, 0));
        }
    }

    private List<Charge> charges = new ArrayList<>();

    Field2D addCharge(double x, double y, double q) {
        charges.add(new Charge(x, y, q));
        return this;
    }

    double[] getMinMax(DoublePoint from, DoublePoint to, double dx, double dy) {
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

    double getValue(double x, double y) {
        return charges.parallelStream()
                .mapToDouble(c -> c.influenceOn(x, y))
                .sum();
    }
}
