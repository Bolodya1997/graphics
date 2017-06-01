package ru.nsu.fit.g14203.popov.raytracing.tracing;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.awt.*;

class Ray {

    Vector from;
    Vector direction;
    float[] intense = { 0, 0, 0 };

    Ray(Vector from, Vector direction) {
        this.from = from;
        this.direction = direction;
    }
}
