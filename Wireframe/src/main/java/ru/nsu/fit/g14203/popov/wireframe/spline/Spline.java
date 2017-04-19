package ru.nsu.fit.g14203.popov.wireframe.spline;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Spline {

    private static class Segment {
        Point2D from;
        Point2D to;
        Point2D curvature;

        Segment(Point2D from, Point2D to) {
            this.from = from;
            this.to = to;

            curvature = from.midpoint(to);
        }

        Point2D getP(double t) {
            Point2D p1 = from.multiply((1 - t) * (1 - t));
            Point2D p2 = curvature.multiply((1 - t) * t);
            Point2D p3 = to.multiply(t * t);

            return p1.add(p2).add(p3);
        }
    }

    private double lineFrom     = 0;
    private double lineTo       = 1;

    private double rotateFrom   = 0;
    private double rotateTo     = Math.PI * 2;

    private int lineCount       = 10;
    private int rotateCount     = 10;

    private ArrayList<Segment> segments = new ArrayList<>();
    {
        Segment basic = new Segment(new Point2D(-0.5, -0.5),
                                    new Point2D(0.5, 0.5));
        basic.curvature = new Point2D(-0.5, 0.5);

        segments.add(basic);
    }
}
