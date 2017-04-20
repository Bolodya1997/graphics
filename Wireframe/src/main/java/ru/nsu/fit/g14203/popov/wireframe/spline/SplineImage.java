package ru.nsu.fit.g14203.popov.wireframe.spline;

import ru.nsu.fit.g14203.popov.util.Sequence;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.lang.Math.round;

class SplineImage extends BufferedImage {

    private final static int COUNT = 100;

    SplineImage(int width, int height, Spline spline) {
        super(width, height, TYPE_INT_RGB);

        double scaleX = width - 1;
        double scaleY = height - 1;

        Graphics2D g2D = createGraphics();
        g2D.setColor(spline.getColor());
        g2D.setStroke(new BasicStroke(1.5f));

        Sequence sequence = new Sequence(0, 1, COUNT);
        Arrays.stream(spline.getSegments())
                .flatMap(segment -> Stream.generate(() -> segment.getP(sequence.next())).limit(COUNT))
                .reduce((p1, p2) -> {
                    g2D.drawLine((int) round(p1.getX() * scaleX), (int) round(p1.getY() * scaleY),
                                 (int) round(p2.getX() * scaleX), (int) round(p2.getY() * scaleY));
                    return p2;
                });
    }
}
