package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

class Camera {

    final Vector position = new Vector(0, 0, -10);
    private final Vector viewPoint = new Vector(0, 0, 0);
    private final Vector up = new Vector(0, 1, 0);

    final Vector axisZ = viewPoint.copy()
            .shift(position.copy().resize(-1))
            .normalize();
    final Vector axisX = axisZ.copy().
            crossProduct(up).
            normalize();
    final Vector axisY = axisZ.copy().
            crossProduct(axisX).
            normalize();

    private double frontZ = 5;

    void move(double offset) {
        frontZ -= offset * 0.05;
    }

    int getFrontZ(int size) {
        return (int) Math.round(frontZ * size);
    }

    int getBackZ(int size) {
        return (int) Math.round((frontZ + 0.8) * size);
    }
}
