package ru.nsu.fit.g14203.popov.isolines;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.stream.Stream;

class Isoline {

    private Stream.Builder<Point2D.Double[]> edges = Stream.builder();

    private Point2D.Double from;
    private Point2D.Double to;

    Isoline(int gridWidth, int gridHeight,
            Point2D.Double from, Point2D.Double to,
            Function2D function, double level) {
        this.from = from;
        this.to = to;

        double cellWidth = (to.getX() - from.getX()) / gridWidth;
        double cellHeight = (to.getY() - from.getY()) / gridHeight;

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point2D.Double[] corners = Stream.generate(Point2D.Double::new)
                        .limit(4)
                        .toArray(Point2D.Double[]::new);
                for (int i = 0; i < 4; i++)
                    corners[i].setLocation(from.getX() + (gridX + i % 2) * cellWidth,
                                           from.getY() + (gridY + i / 2) * cellHeight);

                computeCell(cellWidth, cellHeight,
                            from.getX() + gridX * cellWidth, from.getY() + gridY * cellHeight,
                            corners,
                            function, level);
            }
        }
    }

    private void computeCell(double cellWidth, double cellHeight,
                             double offsetX, double offsetY,
                             Point2D.Double[] corners,
                             Function2D function, double level) {
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
                edges.add(new Point2D.Double[] { p01, p02 });
                break;

            /*
             *  0 . 1
             *      .
             *  2   3
             */
            case 0b0100:
            case 0b1011:
                edges.add(new Point2D.Double[] { p01, p13 });
                break;

            /*
             *  0   1
             *  .
             *  2 . 3
             */
            case 0b0010:
            case 0b1101:
                edges.add(new Point2D.Double[] { p23, p02 });
                break;

            /*
             *  0   1
             *      .
             *  2 . 3
             */
            case 0b0001:
            case 0b1110:
                edges.add(new Point2D.Double[] { p23, p13 });
                break;

            /*
             *  0   1
             *  .   .
             *  2   3
             */
            case 0b0011:
            case 0b1100:
                edges.add(new Point2D.Double[] { p02, p13 });
                break;

            /*
             *  0 . 1
             *
             *  2 . 3
             */
            case 0b0101:
            case 0b1010:
                edges.add(new Point2D.Double[] { p01, p23 });
                break;

            /*
             *  0 . 1
             *  .   .
             *  2 . 3
             */
            case 0b0110:
            case 0b1001:
                double center = function.getValue((corners[3].getX() - corners[0].getX()) / 2,
                                                  (corners[3].getY() - corners[0].getX()) / 2);
                type ^= (center < 0) ? 0 : 0b1111;
                if (type == 0b0110) {
                    edges.add(new Point2D.Double[] { p01, p13 });
                    edges.add(new Point2D.Double[] { p23, p02 });
                } else {
                    edges.add(new Point2D.Double[] { p01, p02 });
                    edges.add(new Point2D.Double[] { p23, p13 });
                }
        }
    }

    Point2D.Double getFrom() {
        return from;
    }

    Point2D.Double getTo() {
        return to;
    }

    Point2D.Double[][] getEdges() {
        return edges.build().toArray(Point2D.Double[][]::new);
    }
}
