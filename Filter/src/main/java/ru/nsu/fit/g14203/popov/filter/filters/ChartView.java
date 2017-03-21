package ru.nsu.fit.g14203.popov.filter.filters;

import javax.swing.*;
import java.awt.*;

class ChartView extends JPanel {

    private Point[] points;

    void setPoints(Point[] points) {
        this.points = points;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
