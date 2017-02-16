package ru.nsu.fit.g14203.popov.life;

import ru.nsu.fit.g14203.popov.life.util.MyPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel {

    private Grid grid = new Grid(10, 10);

    private int size = 45;

    private BufferedImage canvas;

    private Color deadColor = new Color(0xFF8579);
    private Color aliveColor = new Color(0x63FF82);

    GamePanel() {
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
    }

    Grid getGrid() {
        return grid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int width = GridInfo.getWidth(grid.getWidth(), size);
        int height = GridInfo.getHeight(grid.getHeight(), size);

        setPreferredSize(new Dimension(width, height));

        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        canvas.getGraphics().fillRect(0, 0, width, height);

        drawGrid();

        g.drawImage(canvas, 0, 0, null);
    }

    private void drawGrid() {
        for (int gridX = 0; gridX < grid.getWidth(); gridX++) {
            for (int gridY = 0; gridY < grid.getHeight(); gridY++) {
                Point[] points = GridInfo.getPoints(gridX, gridY, size);
                for (int p = 0; p < 6; p++) {
                    MyPainter.drawLine(canvas, points[p].x, points[p].y,
                            points[(p + 1) % 6].x, points[(p + 1) % 6].y, Color.BLACK);
                }

                Point point = GridInfo.getPoint(gridX, gridY, size);
                if (grid.getValue(gridX, gridY) == 0.0)
                    MyPainter.fillArea(canvas, point.x, point.y, deadColor);
                else
                    MyPainter.fillArea(canvas, point.x, point.y, aliveColor);
            }
        }
    }

    private void paintCell(MouseEvent e) {
        if (e.getX() < 0 || e.getX() >= canvas.getWidth())
            return;
        if (e.getY() < 0 || e.getY() >= canvas.getHeight())
            return;

        int RGB = canvas.getRGB(e.getX(), e.getY()) & 0xFFFFFF;
        if (RGB == 0x000000 || RGB == 0xFFFFFF)
            return;

        Point point = GridInfo.getGridPosition(e.getX(), e.getY(), size);
        if (point.x == -1)
            return;

        double oldValue = grid.getValue(point.x, point.y);
        if (oldValue == 1.0)
            return;

        grid.setValue(point.x, point.y, 1.0);
        repaint();
    }
}
