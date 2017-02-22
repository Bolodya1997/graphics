package ru.nsu.fit.g14203.popov.life;

import java.awt.*;

class Grid {

    private class Cell {
        double impact = 0.0;
        boolean alive = false;

        void recountAlive() {
            if (alive)
                alive = Grid.this.lifeBegin <= impact && impact <= Grid.this.lifeEnd;
            else
                alive = Grid.this.birthBegin <= impact && impact <= Grid.this.birthEnd;
        }
    }

    private double lifeBegin = 6.0;
    private double lifeEnd = 6.0;

    private double birthBegin = 1.0;
    private double birthEnd = 6.0;

    private double firstImpact = 1.0;
    private double secondImpact = 0.0;

    private int width;
    private int height;

    private static final int WIDTH_BORDER = 2;
    private static final int HEIGHT_BORDER = 2;

    private Cell[][] grid;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;

        grid = new Cell[width + WIDTH_BORDER * 2][height + HEIGHT_BORDER * 2];
        for (Cell[] row : grid) {
            for (int i = 0; i < row.length; i++)
                row[i] = new Cell();
        }
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    double getImpact(int gridX, int gridY) {
        return grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].impact;
    }

    void setAlive(int gridX, int gridY, boolean alive) {
        grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].alive = alive;
        recountImpact();
    }

    boolean getAlive(int gridX, int gridY) {
        return grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].alive;
    }

    void step() {
        for (int x = 1; x < width + WIDTH_BORDER; x++) {
            for (int y = 1; y < height + HEIGHT_BORDER; y++)
                grid[x][y].recountAlive();
        }
        recountImpact();
    }

    void clear() {
        for (int x = 1; x < width + WIDTH_BORDER; x++) {
            for (int y = 1; y < height + HEIGHT_BORDER; y++) {
                grid[x][y].impact = 0.0;
                grid[x][y].alive = false;
            }
        }
    }

    private void recountImpact() {
        for (int x = WIDTH_BORDER; x < width + WIDTH_BORDER; x++) {
            for (int y = HEIGHT_BORDER; y < height + HEIGHT_BORDER; y++)
                recountImpact(x, y);
        }
    }

    private void recountImpact(int x, int y) {
        boolean even = ((y - HEIGHT_BORDER) % 2 == 0);

        grid[x][y].impact = 0;

        Point[] firstOffsets = { new Point(even ? -1 : 0, -1), new Point(even ? 0 : 1, -1),
                                 new Point(-1, 0), new Point(1, 0),
                                 new Point(even ? -1 : 0, 1), new Point(even ? 0 : 1, 1) };
        for (Point offset : firstOffsets)
            grid[x][y].impact += grid[x + offset.x][y + offset.y].alive ? firstImpact : 0;

        Point[] secondOffsets = { new Point(0, -2),
                                  new Point(even ? -2 : -1, -1), new Point(even ? 1 : 2, -1),
                                  new Point(even ? -2 : -1, 1), new Point(even ? 1 : 2, 1),
                                  new Point(0, 2) };
        for (Point offset : secondOffsets)
            grid[x][y].impact += grid[x + offset.x][y + offset.y].alive ? secondImpact : 0;
    }
}
