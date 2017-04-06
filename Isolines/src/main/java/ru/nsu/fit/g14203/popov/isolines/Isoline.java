package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.stream.Stream;

class Isoline {
    private Point2D.Double[] points;
    private Stream.Builder<Point2D> __points = Stream.builder();

    private Point2D.Double[][] edges;
    private Stream.Builder<Point2D.Double[]> __edges = Stream.builder();

    private Function2D function;

    private Point2D.Double from;
    private Point2D.Double to;

    private double cellWidth;
    private double cellHeight;

    private double level;

    Isoline(int gridWidth, int gridHeight,
            Point2D.Double from, Point2D.Double to,
            Function2D function, double level) {
        this.function = function;
        this.from = from;
        this.to = to;
        this.level = level;

        cellWidth = (to.getX() - from.getX()) / gridWidth;
        cellHeight = (to.getY() - from.getY()) / gridHeight;

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point2D.Double[] corners = Stream.generate(Point2D.Double::new)
                        .limit(4)
                        .toArray(Point2D.Double[]::new);
                for (int i = 0; i < 4; i++)
                    corners[i].setLocation(from.getX() + (gridX + i % 2) * cellWidth,
                                           from.getY() + (gridY + i / 2) * cellHeight);

                computeCell(from.getX() + gridX * cellWidth, from.getY() + gridY * cellHeight,
                            corners);
            }
        }

        points = __points.build().toArray(Point2D.Double[]::new);
        edges = __edges.build().toArray(Point2D.Double[][]::new);
    }

    private void computeCell(double offsetX, double offsetY,
                             Point2D.Double[] corners) {
        double[] f = Arrays.stream(corners)
                .mapToDouble(p -> function.getValue(p.getX(), p.getY()))
                .toArray();

        Point2D.Double p01 = new Point2D.Double();
        p01.setLocation(offsetX + (f[0] - level) / (f[0] - f[1]) * cellWidth,
                        offsetY + 0);

        Point2D.Double p23 = new Point2D.Double();
        p23.setLocation(offsetX + (f[2] - level) / (f[2] - f[3]) * cellWidth,
                        offsetY + cellHeight);

        Point2D.Double p02 = new Point2D.Double();
        p02.setLocation(offsetX + 0,
                        offsetY + (f[0] - level) / (f[0] - f[2]) * cellHeight);

        Point2D.Double p13 = new Point2D.Double();
        p13.setLocation(offsetX + cellWidth,
                        offsetY + (f[1] - level) / (f[1] - f[3]) * cellHeight);

        int type = 0;
        for (int i = 0; i < 4; i++) {
            type <<= 1;
            type |= (f[i] <= level) ? 0 : 1;
        }

        if (type == 0) {
            for (int i = 0; i < 4; i++) {
                type <<= 1;
                type |= (f[i] < level) ? 0 : 1;
            }
        }

        switch (type) {
            case 0b0000:
            case 0b1111:
                break;

            /*
             *  0 . 1
             *  .
             *  2   3
             */
            case 0b1000:
            case 0b0111:
                __points.add(p01);
                __points.add(p02);

                __edges.add(new Point2D.Double[] { p01, p02 });

                break;

            /*
             *  0 . 1
             *      .
             *  2   3
             */
            case 0b0100:
            case 0b1011:
                __points.add(p01);
                __points.add(p13);

                __edges.add(new Point2D.Double[] { p01, p13 });

                break;

            /*
             *  0   1
             *  .
             *  2 . 3
             */
            case 0b0010:
            case 0b1101:
                __points.add(p23);
                __points.add(p02);

                __edges.add(new Point2D.Double[] { p23, p02 });

                break;

            /*
             *  0   1
             *      .
             *  2 . 3
             */
            case 0b0001:
            case 0b1110:
                __points.add(p23);
                __points.add(p13);

                __edges.add(new Point2D.Double[] { p23, p13 });

                break;

            /*
             *  0   1
             *  .   .
             *  2   3
             */
            case 0b0011:
            case 0b1100:
                __points.add(p02);
                __points.add(p13);

                __edges.add(new Point2D.Double[] { p02, p13 });

                break;

            /*
             *  0 . 1
             *
             *  2 . 3
             */
            case 0b0101:
            case 0b1010:
                __points.add(p01);
                __points.add(p23);

                __edges.add(new Point2D.Double[] { p01, p23 });

                break;

            /*
             *  0 . 1
             *  .   .
             *  2 . 3
             */
            case 0b0110:
            case 0b1001:
                __points.add(p01);
                __points.add(p23);
                __points.add(p02);
                __points.add(p13);

                double center = function.getValue((corners[3].getX() - corners[0].getX()) / 2,
                                                  (corners[3].getY() - corners[0].getX()) / 2);
                type ^= (center < level) ? 0b1111 : 0;
                if (type == 0b0110) {
                    __edges.add(new Point2D.Double[] { p01, p13 });
                    __edges.add(new Point2D.Double[] { p23, p02 });
                } else {
                    __edges.add(new Point2D.Double[] { p01, p02 });
                    __edges.add(new Point2D.Double[] { p23, p13 });
                }
        }
    }

    Point2D.Double getFrom() {
        return from;
    }

    Point2D.Double getTo() {
        return to;
    }

    Point2D.Double[] getPoints() {
        return points;
    }

    Point2D.Double[][] getEdges() {
        return edges;
    }
}
