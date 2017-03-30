package ru.nsu.fit.g14203.popov.isolines;

import java.util.Arrays;
import java.util.stream.Stream;

class Isoline {

    private Stream.Builder<DoublePoint[]> edges = Stream.builder();

    Isoline(int gridWidth, int gridHeight,
            DoublePoint from, DoublePoint to,
            Field2D function, double level) {

        double cellWidth = (to.getX() - from.getX()) / gridWidth;
        double cellHeight = (to.getY() - from.getY()) / gridHeight;

        for (int gridX = 0; gridX < gridWidth - 1; gridX++) {
            for (int gridY = 0; gridY < gridHeight - 1; gridY++) {
                DoublePoint[] corners = Stream.generate(DoublePoint::new).limit(4).toArray(DoublePoint[]::new);
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
                             DoublePoint[] corners,
                             Field2D function, double level) {
        double[] f = Arrays.stream(corners)
                .mapToDouble(p -> function.getValue(p.getX(), p.getY()))
                .toArray();

        DoublePoint p01 = new DoublePoint();
        p01.setLocation(offsetX + /*(Math.max(f[0], f[1]) - level) * Math.abs(f[0] - f[1]) **/ cellWidth / 2,
                            offsetY + 0);

        DoublePoint p23 = new DoublePoint();
        p23.setLocation(offsetX + /*(Math.max(f[2], f[3]) - level) / Math.abs(f[2] - f[3]) **/ cellWidth / 2,
                        offsetY + cellHeight);

        DoublePoint p02 = new DoublePoint();
        p02.setLocation(offsetX + 0,
                        offsetY + /*(Math.max(f[0], f[2]) - level) / Math.abs(f[0] - f[2]) **/ cellHeight / 2);

        DoublePoint p13 = new DoublePoint();
        p13.setLocation(offsetX + cellWidth,
                        offsetY + /*(Math.max(f[1], f[3]) - level) / Math.abs(f[1] - f[3]) **/ cellHeight / 2);

//        if (f[0] == f[1])
//            p01.setLocation(corners[0]);
//        if (f[1] == f[3])
//            p13.setLocation(corners[1]);
//        if (f[0] == f[2])
//            p02.setLocation(corners[2]);
//        if (f[2] == f[3])
//            p23.setLocation(corners[3]);

        int type = 0;
        for (int i = 0; i < 4; i++, type <<= 1)
            type |= (f[i] < level) ? 0 : 1;

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
                edges.add(new DoublePoint[] { p01, p02 });
                break;

            /*
             *  0 . 1
             *      .
             *  2   3
             */
            case 0b0100:
            case 0b1011:
                edges.add(new DoublePoint[] { p01, p13 });
                break;

            /*
             *  0   1
             *  .
             *  2 . 3
             */
            case 0b0010:
            case 0b1101:
                edges.add(new DoublePoint[] { p23, p02 });
                break;

            /*
             *  0   1
             *      .
             *  2 . 3
             */
            case 0b0001:
            case 0b1110:
                edges.add(new DoublePoint[] { p23, p13 });
                break;

            /*
             *  0   1
             *  .   .
             *  2   3
             */
            case 0b0011:
            case 0b1100:
                edges.add(new DoublePoint[] { p02, p13 });
                break;

            /*
             *  0 . 1
             *
             *  2 . 3
             */
            case 0b0101:
            case 0b1010:
                edges.add(new DoublePoint[] { p01, p23 });
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
                if (type == 0b0110){
                    edges.add(new DoublePoint[] { p01, p13 });
                    edges.add(new DoublePoint[] { p23, p02 });
                } else {
                    edges.add(new DoublePoint[] { p01, p02 });
                    edges.add(new DoublePoint[] { p23, p13 });
                }
        }
    }

    DoublePoint[][] getEdges() {
        return edges.build().toArray(DoublePoint[][]::new);
    }
}
