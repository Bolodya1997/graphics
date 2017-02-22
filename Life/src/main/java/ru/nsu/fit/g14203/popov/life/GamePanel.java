package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MyPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel {

    private Grid grid;
    private int size;
    private int width;

    private BufferedImage canvas;

    private boolean changed = true;

    private Color deadColor = new Color(0xFF8579);
    private Color aliveColor = new Color(0x63FF82);

    GamePanel() {
        createGrid(100, 100, 1, 10);

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
            changed = true;
            repaint();
        }).start();
    }

    void createGrid(int gridWidth, int gridHeight, int width, int size) {
        grid = new Grid(gridWidth, gridHeight);
        this.width = width;
        this.size = size;

        int pixelWidth = GridInfo.getWidth(grid.getWidth(), size);
        int pixelHeight = GridInfo.getHeight(grid.getHeight(), size);
        canvas = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        canvas.getGraphics().fillRect(0, 0, pixelWidth, pixelHeight);

        drawGrid();
    }

    void clearGrid() {
        grid.clear();
        changed = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = GridInfo.getWidth(grid.getWidth(), size);
        int height = GridInfo.getHeight(grid.getHeight(), size);

        setPreferredSize(new Dimension(width, height));

        if (changed) {
            fillCells();
            changed = false;
        }

        g.drawImage(canvas, 0, 0, null);
    }

    private void drawGrid() {
        Graphics2D g = canvas.createGraphics();
        g.setStroke(new BasicStroke(width));
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < grid.getWidth(); gridX++) {
            for (int gridY = 0; gridY < grid.getHeight(); gridY++) {
                Point[] points = GridInfo.getPoints(gridX, gridY, size);
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
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.BLACK);

        for (int gridX = 0; gridX < grid.getWidth(); gridX++) {
            for (int gridY = 0; gridY < grid.getHeight(); gridY++) {
                Point point = GridInfo.getPoint(gridX, gridY, size);
                if (grid.getAlive(gridX, gridY))
                    MyPainter.fillArea(canvas, point.x, point.y, aliveColor);
                else
                    MyPainter.fillArea(canvas, point.x, point.y, deadColor);

//                char[] tmp = String.format("%.1f", grid.getImpact(gridX, gridY)).toCharArray();
//                g.drawChars(tmp, 0, tmp.length, point.x - 8, point.y + 8);
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

        Point pos = GridInfo.getGridPosition(e.getX(), e.getY(), size);
        if (pos.x < 0 || grid.getWidth() <= pos.x || pos.y < 0 || grid.getHeight() <= pos.y)
            return;

        grid.setAlive(pos.x, pos.y, true);

        changed = true;
//        MyPainter.fillArea(canvas, e.getX(), e.getY(), aliveColor);
        repaint();
    }
}
