package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.ReversedBufferedImage;

import java.awt.*;
import java.awt.geom.Point2D;

class PointsImage extends ReversedBufferedImage {

    PointsImage(int width, int height,
                Isoline isoline, Color pointsColor) {
        super(width, height, TYPE_INT_ARGB);

        Point2D.Double from = isoline.getFrom();
        Point2D.Double to = isoline.getTo();

        double pixelX = width / (to.getX() - from.getX());
        double pixelY = height / (to.getY() - from.getY());

        Graphics2D g2D = createGraphics();
        g2D.setColor(pointsColor);
        for (Point2D.Double point : isoline.getPoints()) {
            int x = (int) ((point.getX() - from.getX()) * pixelX + 0.5) - 2;
            int y = reverseY((int) ((point.getY() - from.getY()) * pixelY + 0.5)) - 2;
            g2D.fillOval(x, y, 5, 5);
        }
    }
}
