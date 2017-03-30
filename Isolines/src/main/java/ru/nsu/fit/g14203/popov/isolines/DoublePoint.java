package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;

class DoublePoint extends Point2D {

    private double x;
    private double y;

    DoublePoint() {
    }

    DoublePoint(double x, double y) {
        setLocation(x, y);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
