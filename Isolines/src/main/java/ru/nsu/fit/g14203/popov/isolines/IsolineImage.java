package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.ReversedBufferedImage;

import java.awt.*;
import java.awt.geom.Point2D;

class IsolineImage extends ReversedBufferedImage {

    IsolineImage(int width, int height,
                 Isoline isoline, Color isolineColor) {
        super(width, height, TYPE_INT_ARGB);

        Point2D.Double from = isoline.getFrom();
        Point2D.Double to = isoline.getTo();

        double pixelX = width / (to.getX() - from.getX());
        double pixelY = height / (to.getY() - from.getY());

        Graphics g = getGraphics();
        g.setColor(isolineColor);
        for (Point2D.Double[] edge : isoline.getEdges()) {
            int x1 = (int) ((edge[0].getX() - from.getX()) * pixelX + 0.5);
            int y1 = reverseY((int) ((edge[0].getY() - from.getY()) * pixelY + 0.5));
            int x2 = (int) ((edge[1].getX() - from.getX()) * pixelX + 0.5);
            int y2 = reverseY((int) ((edge[1].getY() - from.getY()) * pixelY + 0.5));
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
