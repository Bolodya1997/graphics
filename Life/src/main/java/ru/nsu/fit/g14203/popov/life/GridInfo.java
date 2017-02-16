package ru.nsu.fit.g14203.popov.life;

import java.awt.*;
import java.util.ArrayList;

class GridInfo {

    private static final Point badPoint = new Point(-1, -1);

    private static int width(int size) {
        return size * 1732 / 1000;
    }

    private static int height(int size) {
        return size * 2;
    }

    /**
     * @param gridWidth     in cells
     * @param size
     * @return              grid width in pixels
     */
    static int getWidth(int gridWidth, int size) {
        return width(size) * (gridWidth + 1);
    }

    /**
     * @param gridHeight    in cells
     * @param size
     * @return              grid height in pixels
     */
    static int getHeight(int gridHeight, int size) {
        return (size * 3 / 2) * (gridHeight + 1);
    }

    /**
     * @param gridX
     * @param gridY
     * @param size
     * @return              all coordinates of hexagonal cell
     */
    static Point[] getPoints(int gridX, int gridY, int size) {
        Point[] points = new Point[6];
        if (gridY % 2 == 0) {
            points[0] = new Point(width(size) / 2, 0);
            points[1] = new Point(0, size / 2);
        } else {
            points[0] = new Point(width(size), size * 3 / 2);
            points[1] = new Point(width(size) / 2, height(size));
        }
        points[0].x += width(size) * gridX;
        points[0].y += (height(size) + size) * (gridY / 2);
        points[1].x += width(size) * gridX;
        points[1].y += (height(size) + size) * (gridY / 2);

        points[2] = new Point(points[1].x, points[1].y + size);
        points[3] = new Point(points[0].x, points[0].y + height(size));
        points[4] = new Point(points[2].x + width(size), points[2].y);
        points[5] = new Point(points[4].x, points[1].y);

        return points;
    }

    /**
     * @param gridX
     * @param gridY
     * @param size
     * @return              coordinates of hexagonal cell center
     */
    static Point getPoint(int gridX, int gridY, int size) {
        Point base = getPoints(gridX, gridY, size)[0];
        return new Point(base.x, base.y + size);
    }

    /**
     * @param x             x coordinate
     * @param y             y coordinate
     * @param size
     * @return              position in grid
     */
    static Point getGridPosition(int x, int y, int size) {
        int subGridX = x / width(size);
        int subGridY = y / size;

        if (subGridY == 0) {
            return new Point(subGridX, 0);
        }

        switch (subGridY % 3) {
            case 0:
                return line0(subGridX, subGridY, x, y, size);
            case 1:
                return line1(subGridX, subGridY, x, y, size);
            case 2:
                return line2(subGridX, subGridY, x, y, size);
            default:
                return badPoint;
        }
    }

    private static Point line0(int subGridX, int subGridY, int x, int y, int size) {
        Point[] neighbours = new Point[3];

        neighbours[0] = new Point(subGridX, subGridY / 3 * 2);
        neighbours[1] = new Point(neighbours[0].x - 1, neighbours[0].y - 1);
        neighbours[2] = new Point(neighbours[0].x, neighbours[0].y - 1);

        return getClosest(x, y, size, neighbours);
    }

    private static Point line1(int subGridX, int subGridY, int x, int y, int size) {
        Point[] neighbours = new Point[3];

        neighbours[0] = new Point(subGridX, subGridY / 3 * 2);
        neighbours[1] = new Point(neighbours[0].x - 1, neighbours[0].y + 1);
        neighbours[2] = new Point(neighbours[0].x, neighbours[0].y + 1);

        return getClosest(x, y, size, neighbours);
    }

    private static Point line2(int subGridX, int subGridY, int x, int y, int size) {
        if (subGridX == 0)
            return new Point(subGridX, line2Y(subGridY));

        Point[] neighbours = new Point[2];
        neighbours[0] = new Point(subGridX - 1, line2Y(subGridY));
        neighbours[1] = new Point(subGridX, line2Y(subGridY));

        return getClosest(x, y, size, neighbours);
    }

    private static int line2Y(int subGridY) {
        return subGridY / 3 * 2 + 1;
    }

    private static Point getClosest(int x, int y, int size, Point[] neighbours) {
        int min = size * 10;

        Point closest = null;
        for (Point point : neighbours) {
            Point cur = getPoint(point.x, point.y, size);
            int dist = Math.abs(cur.x - x) + Math.abs(cur.y - y);
            if (dist < min) {
                min = dist;
                closest = point;
            }
        }
        return closest;
    }
}
