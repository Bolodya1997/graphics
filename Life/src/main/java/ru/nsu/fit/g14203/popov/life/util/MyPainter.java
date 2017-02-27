package ru.nsu.fit.g14203.popov.life.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class MyPainter {

    /**
     * Draw line between 2 points using Bresenham's line algorithm.
     *
     * @param canvas
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param color
     */
    public static void drawLine(BufferedImage canvas,
                                int x1, int y1, int x2, int y2,
                                Color color) {
        int deltaX = Math.abs(x1 - x2);
        int deltaY = Math.abs(y1 - y2);

        int stepX = (x1 < x2) ? 1 : -1;
        int stepY = (y1 < y2) ? 1 : -1;

        if (deltaX > deltaY)
            drawLineX(canvas, x1, y1, deltaX, deltaY, stepX, stepY, color);
        else
            drawLineY(canvas, x1, y1, deltaX, deltaY, stepX, stepY, color);
    }

    private static void drawLineX(BufferedImage canvas,
                                  int x, int y, int deltaX, int deltaY, int stepX, int stepY,
                                  Color color) {
        int error = 0;
        for (int i = 0; i < deltaX; i++, x += stepX) {
            try {
                canvas.setRGB(x, y, color.getRGB());
            } catch (Exception e) {
                e.printStackTrace();
            }

            error += deltaY;
            if (error * 2 > deltaX) {
                y += stepY;
                error -= deltaX;
            }
        }
    }

    private static void drawLineY(BufferedImage canvas,
                                  int x, int y, int deltaX, int deltaY, int stepX, int stepY,
                                  Color color) {
        int error = 0;
        for (int i = 0; i < deltaY; i++, y += stepY) {
            canvas.setRGB(x, y, color.getRGB());

            error += deltaX;
            if (error * 2 > deltaY) {
                x += stepX;
                error -= deltaY;
            }
        }
    }

    /**
     * Atom element for span filling algorithm.
     */
    private static class Span {

        int x1;
        int x2;
        int y;

        Span(int x1, int x2, int y) {
            this.x1 = x1;
            this.x2 = x2;
            this.y = y;
        }
    }

    /**
     * Fill area around point with same color using span filling algorithm.
     *
     * @param canvas
     * @param x
     * @param y
     * @param color
     */
    public static void fillArea(BufferedImage canvas,
                                int x, int y,
                                Color color) {
        int oldColor = canvas.getRGB(x, y);
        if (oldColor == color.getRGB())
            return;

        LinkedList<Span> stack = new LinkedList<>();
        stack.push(fillSpan(canvas, x, y, oldColor, color.getRGB()));

        while (!stack.isEmpty()) {
            Span cur = stack.pop();
            if (cur.y <= 0 || cur.y >= canvas.getHeight() - 1)
                continue;

            for (int __x = cur.x1; __x <= cur.x2; __x++) {
                if (canvas.getRGB(__x, cur.y + 1) == oldColor)
                    stack.push(fillSpan(canvas, __x, cur.y + 1, oldColor, color.getRGB()));
                if (canvas.getRGB(__x, cur.y - 1) == oldColor)
                    stack.push(fillSpan(canvas, __x, cur.y - 1, oldColor, color.getRGB()));
            }
        }
    }

    private static Span fillSpan(BufferedImage canvas,
                                 int x, int y,
                                 int oldColor, int newColor) {
        int x1, x2;

        for (x1 = x; x1 > 0 && canvas.getRGB(x1, y) == oldColor; x1--);

        if (canvas.getRGB(x1, y) != oldColor)
            ++x1;

        for (x2 = x1; x2 < canvas.getWidth() - 1 && canvas.getRGB(x2, y) == oldColor; x2++)
            canvas.setRGB(x2, y, newColor);

        if (canvas.getRGB(x2, y) != oldColor)
            --x2;

        return new Span(x1, x2, y);
    }
}
