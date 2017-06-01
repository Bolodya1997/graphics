package ru.nsu.fit.g14203.popov.util;

public class Sequence {

    private final double from;
    private final int count;

    private double value;
    private double step;
    private int c = 0;

    public Sequence(double from, double to, int count) {
        this.from = from;
        this.count = count;

        value = from;
        step = (to - from) / (count - 1);
    }

    public double next() {
        double next = value;

        if (count == ++c) {
            c = 0;
            value = from;
        } else {
            value += step;
        }

        return next;
    }
}
