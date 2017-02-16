package ru.nsu.fit.g14203.popov.life;

class Grid {

    private int width;
    private int height;

    private double[][] grid;

    Grid(int width, int height) {
        this.width = width;
        this.height = height;

        this.grid = new double[width][height];
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
    }

    double getValue(int gridX, int gridY) {
        return grid[gridX][gridY];
    }

    void setValue(int gridX, int gridY, double value) {
        grid[gridX][gridY] = value;
    }

    void clear() {
        for (double[] row : grid) {
            for (int i = 0; i < row.length; i++)
                row[i] = 0;
        }
    }
}
