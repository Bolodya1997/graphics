package ru.nsu.fit.g14203.popov.life.util;

public class MutableDouble {

    private double value;

    public MutableDouble(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
