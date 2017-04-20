package ru.nsu.fit.g14203.popov.wireframe.spline;

import ru.nsu.fit.g14203.popov.util.Sequence;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class Spline {

    static class Segment {
        Point2D[] points;

        Segment(Point2D[] points) {
            this.points = points;
        }

        Point2D getP(double t) {
            double[] k = new double[]{
                    -1 * t*t*t +  3 * t*t + -3 * t + 1,
                     3 * t*t*t + -6 * t*t +  0 * t + 4,
                    -3 * t*t*t +  3 * t*t +  3 * t + 1,
                     1 * t*t*t +  0 * t*t +  0 * t + 0,
            };

            Point2D.Double result = new Point2D.Double();
            for (int i = 0; i < points.length; i++) {
                result.x += points[i].getX() * k[i] / 6;
                result.y += points[i].getY() * k[i] / 6;
            }

            return result;
        }
    }

    private double lineFrom     = 0;
    private double lineTo       = 1;

    private double rotateFrom   = 0;
    private double rotateTo     = Math.PI * 2;

    private int lineCount       = 10;
    private int rotateCount     = 10;

    private Color color;
    {
        Random random = new Random();
        color = new Color(random.nextInt(255),
                          random.nextInt(255),
                          random.nextInt(255));
    }

    private ArrayList<Point2D> points = new ArrayList<>();
    private Segment[] segments;

    public Spline() {
        Point2D[] __points = new Point2D[]{
                new Point2D.Double(0, 0),
                new Point2D.Double(0.1, 1.0 / 3),
                new Point2D.Double(2.0 / 3, 0.1),
                new Point2D.Double(0.8, 0.5)
        };

        points.add(__points[0]);
        points.add(__points[1]);
        points.add(__points[2]);
        points.add(__points[3]);

        addPoint(4, new Point2D.Double(1, 1));
        addPoint(2, new Point2D.Double(0.13, 0.75));
        addPoint(6, new Point2D.Double(-0.5, -0.2));
    }

    void addPoint(int index, Point2D point2D) {
        points.add(index, point2D);
        Point2D[] __points = points.stream().toArray(Point2D[]::new);

        segments = new Segment[points.size() - 3];
        for (int i = 0; i < segments.length; i++)
            segments[i] = new Segment(Arrays.copyOfRange(__points, i, i + 4));
    }

    Segment[] getSegments() {
        return segments;
    }

    public Point2D[] getPoints() {
        final int size = 10;
        Sequence sequence = new Sequence(0, 1, size);
        return Arrays.stream(segments)
                .flatMap(segment -> Stream.generate(() -> segment.getP(sequence.next())).limit(size))
                .toArray(Point2D[]::new);
    }

    public Color getColor() {
        return color;
    }
}
