package ru.nsu.fit.g14203.popov.filter.filters.rendering;

import ru.nsu.fit.g14203.popov.util.State;

import java.awt.*;
import java.util.stream.Stream;

class Chart {

    private class Border {
        double leftBound;
        double rightBound;

        Border(double value) {
            this.leftBound = value;
            this.rightBound = value;
        }
    }

    private Border[] borders = new Border[101];

    private int lastPos = 0;

    private double maxValue;
    private State enable;

    Chart(double maxValue, State enable) {
        borders[0] = new Border(0);
        this.maxValue = maxValue;
        this.enable = enable;
    }

    void addValue(int pos, double value) {
        if (value < 0 || value > maxValue)
            throw new RuntimeException("Value out of range");

        if (pos == lastPos) {
            borders[pos].rightBound = value;
            return;
        }

        double oldValue = borders[lastPos].rightBound;
        double step = (value - oldValue) / (pos - lastPos);
        for (int i = 1; i <= pos - lastPos; i++)
            borders[lastPos + i] = new Border(oldValue + step * i);

        lastPos = pos;
    }

    void end() {
        addValue(100, 0);
    }

    Point[] getEdges() {
        double yWeight = 100 / maxValue;

        Stream.Builder<Point> builder = Stream.builder();
        for (int i = 0; i < borders.length - 1; i++) {
            builder.add(new Point(i, (int) (borders[i].leftBound * yWeight + 0.5)));
            builder.add(new Point(i, (int) (borders[i].rightBound * yWeight + 0.5)));
        }
        builder.add(new Point(100, (int) (borders[100].leftBound * yWeight + 0.5)));

        return builder.build()
                .distinct()
                .toArray(Point[]::new);
    }

    double getValue(double pos) {
        if (!enable.isTrue())
            return 0;

        if (pos == 0)
            return borders[0].rightBound;
        if (pos == 100)
            return borders[100].leftBound;

        int p = (int) pos;
        double left = borders[p].rightBound;
        double right = borders[p + 1].leftBound;

        return left + (right - left) * (pos - p);
    }
}
