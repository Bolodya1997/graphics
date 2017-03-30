package ru.nsu.fit.g14203.popov.isolines;

import java.awt.*;
import java.awt.image.BufferedImage;

class IsolineImage extends BufferedImage {

    IsolineImage(int width, int height,
                 DoublePoint from, DoublePoint to,
                 Isoline isoline, Color isolineColor) {
        super(width, height, TYPE_INT_ARGB);

        double pixelX = width / (to.getX() - from.getX());
        double pixelY = height / (to.getY() - from.getY());

        Graphics g = getGraphics();
        g.setColor(isolineColor);
        for (DoublePoint[] edge : isoline.getEdges()) {
            int x1 = (int) ((edge[0].getX() - from.getX()) * pixelX + 0.5);
            int y1 = (int) ((edge[0].getY() - from.getY()) * pixelY + 0.5);
            int x2 = (int) ((edge[1].getX() - from.getX()) * pixelX + 0.5);
            int y2 = (int) ((edge[1].getY() - from.getY()) * pixelY + 0.5);
            g.drawLine(x1, y1, x2, y2);
        }
    }
}
