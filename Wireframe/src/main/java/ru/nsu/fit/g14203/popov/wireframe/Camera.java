package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

class Camera {

    final Vector position = new Vector(-10, 0, 0);

    final Vector axisX = new Vector(0, 0, -1);
    final Vector axisY = new Vector(0, 1,  0);
    final Vector axisZ = new Vector(1, 0,  0);

    private double frontZ = 5;

    void move(double offset) {
        frontZ -= offset;
    }

    int getFrontZ(int size) {
        return (int) Math.round(frontZ * size);
    }

    int getBackZ(int size) {
        return (int) Math.round((frontZ + 1) * size);
    }
}
