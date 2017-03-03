package ru.nsu.fit.g14203.popov.life;

import java.awt.*;

class GridInfo {

    private static final Point badPoint = new Point(-1, -1);

    private static int size(int size, int width) {
        return size + width * 30 / 51;
    }

    private static int cellWidth(int size, int width) {
        int tmp = (size(size, width) * 433 / 250);
        return tmp + tmp % 2;
    }

    private static int widthOffsetLeft(int width) {
        return width / 2;
    }

    private static int widthOffsetRight(int width) {
        return width / 2 + width % 2;
    }

    private static int heightOffset(int width) {
        return width / 2 + width % 2;
    }

    private static int cellHeight(int size, int width) {
        return size(size, width) * 2;
    }

    /**
     * @param gridWidth     in cells
     * @param gridHeight    in cells
     * @param size
     * @param width
     * @return              grid cellWidth in pixels
     */
    static int getWidth(int gridWidth, int gridHeight, int size, int width) {
        return (cellWidth(size, width) / 2) * (gridWidth * 2 + ((gridHeight > 1) ? 1 : 0))
                + widthOffsetLeft(width) + widthOffsetRight(width);
    }

    /**
     * @param gridHeight    in cells
     * @param size
     * @param width
     * @return              grid cellHeight in pixels
     */
    static int getHeight(int gridHeight, int size, int width) {
        int height = cellHeight(size, width) + heightOffset(width) * 2;
        if (gridHeight % 2 == 0)
            height += size(size, width) + size(size, width) / 2;

        if (gridHeight <= 2)
            return height;

        return height + (cellHeight(size, width) + size(size, width)) * ((gridHeight - 1) / 2);
}

    /**
     * @param gridX
     * @param gridY
     * @param size
     * @param width
     * @return              all coordinates of hexagonal cell
     */
    static Point[] getPoints(int gridX, int gridY, int size, int width) {
        Point[] points = new Point[6];
        if (gridY % 2 == 0) {
            points[0] = new Point(cellWidth(size, width) / 2, 0);
            points[1] = new Point(0, size(size, width) / 2);
        } else {
            points[0] = new Point(cellWidth(size, width), size(size, width) + size(size, width) / 2);
            points[1] = new Point(cellWidth(size, width) / 2, cellHeight(size, width));
        }
        points[0].x += cellWidth(size, width) * gridX
                + widthOffsetLeft(width);
        points[0].y += (cellHeight(size, width) + size(size, width)) * (gridY >= 0 ? gridY / 2 : (gridY - 1) / 2)
                + heightOffset(width);
        points[1].x += cellWidth(size, width) * gridX
                + widthOffsetLeft(width);
        points[1].y += (cellHeight(size, width) + size(size, width)) * (gridY >= 0 ? gridY / 2 : (gridY - 1) / 2)
                + heightOffset(width);

        points[2] = new Point(points[1].x, points[1].y + size(size, width));
        points[3] = new Point(points[0].x, points[0].y + cellHeight(size, width));
        points[4] = new Point(points[2].x + cellWidth(size, width), points[2].y);
        points[5] = new Point(points[4].x, points[1].y);

        return points;
    }

    /**
     * @param gridX
     * @param gridY
     * @param size
     * @param width
     * @return              coordinates of hexagonal cell center
     */
    static Point getCenter(int gridX, int gridY, int size, int width) {
        Point center;
        if (gridY % 2 == 0)
            center = new Point(cellWidth(size, width) / 2, 0);
        else
            center = new Point(cellWidth(size, width), size(size, width) + size(size, width) / 2);

        center.x += cellWidth(size, width) * gridX
                + widthOffsetLeft(width);
        center.y += (cellHeight(size, width) + size(size, width)) * (gridY >= 0 ? gridY / 2 : (gridY - 1) / 2) + size(size, width)
                + heightOffset(width);

        return center;
    }

    /**
     * @param x             x coordinate
     * @param y             y coordinate
     * @param size
     * @param width
     * @return              position in grid
     */
    static Point getGridPosition(int x, int y, int size, int width) {
        int subGridX = (x - width) / cellWidth(size, width);
        int subGridY = (y - width) / size(size, width);

        switch (subGridY % 3) {
            case 0:
                return line0(subGridX, subGridY, x, y, size, width);
            case 1:
                return line1(subGridX, subGridY, x, y, size, width);
            case 2:
                return line2(subGridX, subGridY, x, y, size, width);
            default:
                return badPoint;
        }
    }

    private static Point line0(int subGridX, int subGridY, int x, int y, int size, int width) {
        Point[] neighbours = new Point[3];

        neighbours[0] = new Point(subGridX, subGridY / 3 * 2);
        neighbours[1] = new Point(neighbours[0].x - 1, neighbours[0].y - 1);
        neighbours[2] = new Point(neighbours[0].x, neighbours[0].y - 1);

        return getClosest(x, y, size, width, neighbours);
    }

    private static Point line1(int subGridX, int subGridY, int x, int y, int size, int width) {
        Point[] neighbours = new Point[3];

        neighbours[0] = new Point(subGridX, subGridY / 3 * 2);
        neighbours[1] = new Point(neighbours[0].x - 1, neighbours[0].y + 1);
        neighbours[2] = new Point(neighbours[0].x, neighbours[0].y + 1);

        return getClosest(x, y, size, width, neighbours);
    }

    private static Point line2(int subGridX, int subGridY, int x, int y, int size, int width) {
        Point[] neighbours = new Point[2];
        neighbours[0] = new Point(subGridX - 1, line2Y(subGridY));
        neighbours[1] = new Point(subGridX, line2Y(subGridY));

        return getClosest(x, y, size, width, neighbours);
    }

    private static int line2Y(int subGridY) {
        return subGridY / 3 * 2 + 1;
    }

    private static Point getClosest(int x, int y, int size, int width, Point[] neighbours) {
        int min = size(size, width) * 10;

        Point closest = null;
        for (Point point : neighbours) {
            Point cur = getCenter(point.x, point.y, size, width);
            int dist = Math.abs(cur.x - x) + Math.abs(cur.y - y);
            if (dist < min) {
                min = dist;
                closest = point;
            }
        }
        return closest;
    }

    /**
     * @param size
     * @return              font size for impact drawing
     */
    static int getFontSize(int size) {
        return size * 3 / 4;
    }

    /**
     * @param gridX
     * @param gridY
     * @param size
     * @param width
     * @param len           length of the impact string (must be 1 or 3)
     * @return              starting point for impact drawing
     */
    static Point getImpactPosition(int gridX, int gridY, int size, int width, int len) {
        Point point = getCenter(gridX, gridY, size, width);
        point.x -= (len == 1) ? size * 8 / 38 : size * 22 / 38;
        point.y += size * 25 / 80;

        return point;
    }
}
