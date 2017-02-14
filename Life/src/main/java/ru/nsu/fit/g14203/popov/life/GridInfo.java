package ru.nsu.fit.g14203.popov.life;

import java.awt.*;
import java.util.stream.Stream;

class GridInfo {

    private static final Point badPoint = new Point(-1, -1);

    private static int width(int size) {
        return size * 1732 / 1000;
    }

    private static int height(int size) {
        return size * 2;
    }

    static int getWidth(int gridWidth, int size) {
        return width(size) * (gridWidth + 1);
    }

    static int getHeight(int gridHeight, int size) {
        return (size * 3 / 2) * (gridHeight + 1);
    }

    static Point[] getPoints(int gridX, int gridY, int size) {
        Point[] points = new Point[6];
        if (gridX == 0) {
            if (gridY == 0) {
                points[0] = new Point(width(size) / 2, 0);
                points[1] = new Point(0, size / 2);
            } else if (gridY == 1) {
                points[0] = new Point(width(size), size * 3 / 2);
                points[1] = new Point(width(size) / 2, height(size));
            } else {
                Point[] base = getPoints(0, gridY % 2, size);
                points[0] = new Point(base[0].x, base[0].y + (height(size) + size) * (gridY / 2));
                points[1] = new Point(base[1].x, base[1].y + (height(size) + size) * (gridY / 2));
            }
            points[2] = new Point(points[1].x, points[1].y + size);
            points[3] = new Point(points[0].x, points[0].y + height(size));
            points[4] = new Point(points[2].x + width(size), points[2].y);
            points[5] = new Point(points[4].x, points[1].y);
        } else {
            points = Stream.of(getPoints(0, gridY, size))
                    .map(p -> new Point(p.x + width(size) * gridX, p.y))
                    .toArray(Point[]::new);
        }

        return points;
    }

    static Point getPoint(int gridX, int gridY, int size) {
        Point base = getPoints(gridX, gridY, size)[0];
        return new Point(base.x, base.y + size);
    }

    static Point getGridPosition(int x, int y, int size) {
        int subGridX = x / (width(size) / 2);
        int subGridY = y / size;

        if (subGridY % 3 == 2) {
            if (subGridX == 0)
                return badPoint;
            return new Point((subGridX - 1) / 2, (subGridY / 3) * 2 + 1);
        }

        return badPoint;
    }
}
