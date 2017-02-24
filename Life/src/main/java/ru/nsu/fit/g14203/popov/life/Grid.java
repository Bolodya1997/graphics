package ru.nsu.fit.g14203.popov.life;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Supplier;

class Grid {

    private class Cell {
        double impact = 0.0;
        boolean alive = false;

        void recountAlive() {
            if (alive)
                alive = Grid.this.settings.lifeBegin.getValue() <= impact
                        && impact <= Grid.this.settings.lifeEnd.getValue();
            else
                alive = Grid.this.settings.birthBegin.getValue() <= impact
                        && impact <= Grid.this.settings.birthEnd.getValue();
        }
    }

    private Settings settings;

    private static final int WIDTH_BORDER = 2;
    private static final int HEIGHT_BORDER = 2;

    private Cell[][] grid;

    static Grid parseStream(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        Supplier<Integer[]> getNextLineParsed = () -> {
            String nextLine, noComments = "";
            while (noComments.isEmpty()) {
                nextLine = scanner.nextLine();
                noComments = nextLine.replaceFirst("//.*", "");
            }

            return Arrays.stream(noComments.split(" "))
                    .map(Integer::decode)
                    .toArray(Integer[]::new);
        };

        Settings settings = new Settings();
        Integer[] parsed = getNextLineParsed.get();
        settings.gridWidth.setValue(parsed[0]);
        settings.gridHeight.setValue(parsed[1]);

        parsed = getNextLineParsed.get();
        settings.width.setValue(parsed[0]);

        parsed = getNextLineParsed.get();
        settings.size.setValue(parsed[0]);

        Grid grid = new Grid(settings);

        parsed = getNextLineParsed.get();
        int count = parsed[0];
        while (count-- > 0) {
            parsed = getNextLineParsed.get();
            int x = parsed[0];
            int y = parsed[1];

            grid.setAlive(x, y, true);
        }

        return grid;
    }

    void printToStream(OutputStream stream) {
        PrintStream output = new PrintStream(stream);
        int alive = 0;
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.alive)
                    ++alive;
            }
        }

        output.printf("%d %d\n", settings.gridWidth.getValue(), settings.gridHeight.getValue());
        output.println(settings.width.getValue());
        output.println(settings.size.getValue());
        output.println(alive);
        for (int x = 0; x < settings.gridWidth.getValue(); x++) {
            for (int y = 0; y < settings.gridHeight.getValue(); y++) {
                if (grid[x + WIDTH_BORDER][y + HEIGHT_BORDER].alive)
                    output.printf("%d %d\n", x, y);
            }
        }

    }

    Grid(Settings settings) {
        this.settings = settings;
        recountGridSize();
    }

    Settings getSettings() {
        return settings;
    }

    void setSettings(Settings settings) {
        this.settings = settings;
        recountGridSize();
    }

    private synchronized void recountGridSize() {
        Cell[][] oldGrid = grid;
        grid = new Cell[settings.gridWidth.getValue() + WIDTH_BORDER * 2]
                       [settings.gridHeight.getValue() + HEIGHT_BORDER * 2];
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid[0].length; y++)
                grid[x][y] = (oldGrid == null) ? new Cell() :
                             (oldGrid.length <= x || oldGrid[0].length <= y) ? new Cell() :
                             (x >= grid.length - WIDTH_BORDER || y >= grid[0].length - HEIGHT_BORDER) ? new Cell() :
                             oldGrid[x][y];
        }
    }

    void clear() {
        for (int x = 1; x < settings.gridWidth.getValue() + WIDTH_BORDER; x++) {
            for (int y = 1; y < settings.gridHeight.getValue() + HEIGHT_BORDER; y++) {
                grid[x][y].impact = 0.0;
                grid[x][y].alive = false;
            }
        }
    }

    double getImpact(int gridX, int gridY) {
        return grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].impact;
    }

    void replace(int gridX, int gridY) {
        setAlive(gridX, gridY, true);
    }

    void xor(int gridX, int gridY) {
        boolean alive = grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].alive;
        setAlive(gridX, gridY, !alive);
    }

    private void setAlive(int gridX, int gridY, boolean alive) {
        grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].alive = alive;
        recountImpact();
    }

    boolean getAlive(int gridX, int gridY) {
        return grid[gridX + WIDTH_BORDER][gridY + HEIGHT_BORDER].alive;
    }

    synchronized void step() {
        for (int x = 1; x < settings.gridWidth.getValue() + WIDTH_BORDER; x++) {
            for (int y = 1; y < settings.gridHeight.getValue() + HEIGHT_BORDER; y++)
                grid[x][y].recountAlive();
        }
        recountImpact();
    }

    private void recountImpact() {
        for (int x = WIDTH_BORDER; x < settings.gridWidth.getValue() + WIDTH_BORDER; x++) {
            for (int y = HEIGHT_BORDER; y < settings.gridHeight.getValue() + HEIGHT_BORDER; y++)
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
            grid[x][y].impact += grid[x + offset.x][y + offset.y].alive ? settings.firstImpact.getValue() : 0;

        Point[] secondOffsets = { new Point(0, -2),
                                  new Point(even ? -2 : -1, -1), new Point(even ? 1 : 2, -1),
                                  new Point(even ? -2 : -1, 1), new Point(even ? 1 : 2, 1),
                                  new Point(0, 2) };
        for (Point offset : secondOffsets)
            grid[x][y].impact += grid[x + offset.x][y + offset.y].alive ? settings.secondImpact.getValue() : 0;
    }
}
