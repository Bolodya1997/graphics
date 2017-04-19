package ru.nsu.fit.g14203.popov.wireframe.spline;

import java.awt.image.BufferedImage;

class SplineImage extends BufferedImage {

    SplineImage(int width, int height, Spline spline) {
        super(width, height, TYPE_INT_RGB);
    }
}
