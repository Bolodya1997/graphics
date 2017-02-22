package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MyPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel {

    private Grid grid = new Grid(new Settings());

    private BufferedImage canvas;
    private Dimension preferredSize;

    private Color deadColor = new Color(0xFF8579);
    private Color aliveColor = new Color(0x63FF82);

    GamePanel() {
        recountGrid(grid.getSettings());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                paintCell(e);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                paintCell(e);
            }
        });

        new Timer(1000, e -> {
            grid.step();
            fillCells();
            repaint();
        }).start();
    }

    Grid getGrid() {
        return grid;
    }

    void recountGrid(Settings settings) {
        grid.setSettings(settings);

        int gridWidth = settings.gridWidth.getValue();
        int gridHeight = settings.gridHeight.getValue();
        int size = settings.size.getValue();
        int width = settings.width.getValue();

        int pixelWidth = GridInfo.getWidth(gridWidth, size, width);
        int pixelHeight = GridInfo.getHeight(gridHeight, size, width);
        preferredSize = new Dimension(pixelWidth, pixelHeight);

        canvas = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        canvas.getGraphics().fillRect(0, 0, pixelWidth, pixelHeight);

        drawGrid();
        fillCells();
        repaint();
    }

    void clearGrid() {
        grid.clear();

        fillCells();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        setPreferredSize(preferredSize);
        g.drawImage(canvas, 0, 0, null);
    }

    private void drawGrid() {
        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        Graphics2D g = canvas.createGraphics();
        g.setStroke(new BasicStroke(width));
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point[] points = GridInfo.getPoints(gridX, gridY, size, width);
                for (int p = 0; p < 6; p++) {
                    if (width == 1)
                        MyPainter.drawLine(canvas, points[p].x, points[p].y,
                                points[(p + 1) % 6].x, points[(p + 1) % 6].y, Color.BLACK);
                    else
                        g.drawLine(points[p].x, points[p].y, points[(p + 1) % 6].x, points[(p + 1) % 6].y);
                }
            }
        }
    }

    private void fillCells() {
        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < gridWidth; gridX++) {
            for (int gridY = 0; gridY < gridHeight; gridY++) {
                Point point = GridInfo.getCenter(gridX, gridY, size, width);
                if (grid.getAlive(gridX, gridY))
                    MyPainter.fillArea(canvas, point.x, point.y, aliveColor);
                else
                    MyPainter.fillArea(canvas, point.x, point.y, deadColor);
            }
        }
    }

    private void paintCell(MouseEvent e) {
        if (e.getX() < 0 || e.getX() >= canvas.getWidth())
            return;
        if (e.getY() < 0 || e.getY() >= canvas.getHeight())
            return;

        int RGB = canvas.getRGB(e.getX(), e.getY()) & 0xFFFFFF;
        if (RGB == 0x000000)
            return;

        int gridWidth = grid.getSettings().gridWidth.getValue();
        int gridHeight = grid.getSettings().gridHeight.getValue();
        int size = grid.getSettings().size.getValue();
        int width = grid.getSettings().width.getValue();

        Point pos = GridInfo.getGridPosition(e.getX(), e.getY(), size, width);
        if (pos.x < 0 || gridWidth <= pos.x || pos.y < 0 || gridHeight <= pos.y)
            return;

        grid.setAlive(pos.x, pos.y, true);

        fillCells();
        repaint();
    }
}
