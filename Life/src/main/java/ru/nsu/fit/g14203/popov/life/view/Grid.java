package ru.nsu.fit.g14203.popov.life.view;

import java.awt.*;
import java.util.stream.Stream;

class Grid {

    private static int height(int size) {
        return size * 2;
    }

    private static int width(int size) {
        return size * 1732 / 1000;
    }

    static Point[] getCoords(int x, int y, int size) {
        Point[] points = new Point[6];
        if (x == 0) {
            if (y == 0) {
                points[0] = new Point(width(size) / 2, 0);
                points[1] = new Point(0, size / 2);
            } else if (y == 1) {
                points[0] = new Point(width(size), size * 3 / 2);
                points[1] = new Point(width(size) / 2, height(size));
            } else {
                Point[] base = getCoords(0, y % 2, size);
                points[0] = new Point(base[0].x, base[0].y + (height(size) + size) * (y / 2));
                points[1] = new Point(base[1].x, base[1].y + (height(size) + size) * (y / 2));
            }
            points[2] = new Point(points[1].x, points[1].y + size);
            points[3] = new Point(points[0].x, points[0].y + height(size));
            points[4] = new Point(points[2].x + width(size), points[2].y);
            points[5] = new Point(points[4].x, points[1].y);
        } else {
            points = Stream.of(getCoords(0, y, size))
                    .map(p -> new Point(p.x + width(size) * x, y))  //  FIXME:  fail!
                    .toArray(Point[]::new);
        }

        return points;
    }

    static Point getPoint(int x, int y, int size) {
        Point point;
        if (x == 0) {
            if (y == 0) {
                point = new Point(width(size) / 2, size / 2);
            } else if (y == 1) {
                point = new Point(width(size), size * 2);
            } else {
                Point[] base = getCoords(0, y % 2, size);
                point = new Point(base[0].x, base[0].y + (height(size) + size) * y / 2 + size / 2);
            }
        } else {
            Point[] base = getCoords(0, y, size);
            point = new Point(base[0].x + width(size) * x, base[0].y + size / 2);
        }

        return point;
    }
}
