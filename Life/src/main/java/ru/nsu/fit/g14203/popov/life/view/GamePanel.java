package ru.nsu.fit.g14203.popov.life.view;

import ru.nsu.fit.g14203.popov.life.view.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel {

    int size = 40;

    GamePanel() {
        new Timer(1, e -> repaint()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(createImage(getWidth(), getHeight()), 0, 0, null);

        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 7; k++) {
                Coordinate[] coords = Cell.getCoords(i, k, size);
                MyPainter.drawLine(image, coords[0].x, coords[0].y, coords[1].x, coords[1].y, Color.BLACK);
                MyPainter.drawLine(image, coords[1].x, coords[1].y, coords[2].x, coords[2].y, Color.BLACK);
                MyPainter.drawLine(image, coords[2].x, coords[2].y, coords[3].x, coords[3].y, Color.BLACK);
                MyPainter.drawLine(image, coords[3].x, coords[3].y, coords[4].x, coords[4].y, Color.BLACK);
                MyPainter.drawLine(image, coords[4].x, coords[4].y, coords[5].x, coords[5].y, Color.BLACK);
                MyPainter.drawLine(image, coords[5].x, coords[5].y, coords[0].x, coords[0].y, Color.BLACK);

                Coordinate point = Cell.getPoint(i, k, size);
                MyPainter.fillArea(image, point.x, point.y, new Color((int) Math.round(Math.random() * 0xFFFFFF)));
            }
        }

        g.drawImage(image, 0, 0, null);
    }
}
