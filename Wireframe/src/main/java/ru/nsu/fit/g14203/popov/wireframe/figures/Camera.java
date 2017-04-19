package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

class Camera {

    final Vector position = new Vector(-5, -3, -10);
    private final Vector viewPoint = new Vector(0, 0, 0);
    private final Vector up = new Vector(6, 1, 4);

    final Vector axisZ = viewPoint.copy()
            .shift(position.copy().resize(-1))
            .normalize();
    final Vector axisX = Vector.crossProduct(axisZ, up).normalize();
    final Vector axisY = Vector.crossProduct(axisZ, axisX).normalize();

    private double frontZ = 5;

    void move(double offset) {
        frontZ -= offset * 0.05;
    }

    int getFrontZ(int size) {
        return (int) Math.round(frontZ * size);
    }

    int getBackZ(int size) {
        return (int) Math.round((frontZ + 1) * size);
    }
}
