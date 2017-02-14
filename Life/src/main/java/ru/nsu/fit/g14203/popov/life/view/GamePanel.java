package ru.nsu.fit.g14203.popov.life.view;

import ru.nsu.fit.g14203.popov.life.view.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class GamePanel extends JPanel {

    int size = 39;

    GamePanel() {
//        new Timer(1, e -> repaint()).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(createImage(getWidth(), getHeight()), 0, 0, null);

        for (int i = 0; i < 2; i++) {
            for (int k = 0; k < 5; k++) {
                Point[] points = Grid.getCoords(i, k, size);
                MyPainter.drawLine(image, points[0].x, points[0].y, points[1].x, points[1].y, Color.BLACK);
                MyPainter.drawLine(image, points[1].x, points[1].y, points[2].x, points[2].y, Color.BLACK);
                MyPainter.drawLine(image, points[2].x, points[2].y, points[3].x, points[3].y, Color.BLACK);
                MyPainter.drawLine(image, points[3].x, points[3].y, points[4].x, points[4].y, Color.BLACK);
                MyPainter.drawLine(image, points[4].x, points[4].y, points[5].x, points[5].y, Color.BLACK);
                MyPainter.drawLine(image, points[5].x, points[5].y, points[0].x, points[0].y, Color.BLACK);

                Point point = Grid.getPoint(i, k, size);
                MyPainter.fillArea(image, point.x, point.y, new Color((int) Math.round(Math.random() * 0xFFFFFF)));
            }
        }

        g.drawImage(image, 0, 0, null);
    }
}
