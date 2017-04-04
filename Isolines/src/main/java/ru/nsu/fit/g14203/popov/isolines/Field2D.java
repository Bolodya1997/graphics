package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

class Field2D extends Function2D {

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

    @Override
    public double getValue(double x, double y) {
        return charges.parallelStream()
                .mapToDouble(c -> c.influenceOn(x, y))
                .sum();
    }
}
