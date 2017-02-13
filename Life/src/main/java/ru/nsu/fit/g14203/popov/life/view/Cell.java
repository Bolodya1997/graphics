package ru.nsu.fit.g14203.popov.life.view;

import ru.nsu.fit.g14203.popov.life.view.util.Coordinate;

import java.awt.*;

class Cell {

    static Coordinate[] getCoords(int x, int y, int size) {
        Coordinate[] coords = new Coordinate[6];

        int height = size * 2;
        int width = (size * 866 * 2) / 1000;

        if (y % 2 == 0) {
            coords[0] = new Coordinate(x * width + width / 2, (y / 2) * (height + size));
            coords[1] = new Coordinate(x * width, coords[0].y + size / 2);
        } else {
            coords[0] = new Coordinate((x + 1) * width, (y - 1) / 2 * (height + size) + size + size / 2);
            coords[1] = new Coordinate(x * width + width / 2, coords[0].y + size / 2);
        }
        coords[2] = new Coordinate(coords[1].x, coords[1].y + size);
        coords[3] = new Coordinate(coords[0].x, coords[0].y + height);
        coords[4] = new Coordinate(coords[2].x + width, coords[2].y);
        coords[5] = new Coordinate(coords[4].x, coords[1].y);

        return coords;
    }

    static Coordinate getPoint(int x, int y, int size) {
        int height = size * 2;
        int width = (size * 866 * 2) / 1000;

        if (y % 2 == 0)
            return new Coordinate(x * width + width / 2, (y / 2) * (height + size) + size / 2);
        else
            return new Coordinate((x + 1) * width, (y - 1) / 2 * (height + size) + height);
    }
}
