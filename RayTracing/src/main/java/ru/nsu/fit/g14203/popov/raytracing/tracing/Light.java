package ru.nsu.fit.g14203.popov.raytracing.tracing;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

public class Light {

    private Vector position;
    private float[] intense;

    public Light(Vector position, float[] intense) {
        this.position = position;
        this.intense = intense;
    }

    Vector getPosition() {
        return position;
    }

    float[] getIntense() {
        return intense;
    }
}
