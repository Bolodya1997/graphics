package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.*;
import java.util.Observable;

public class Camera extends Observable {

    private final static Camera INSTANCE = new Camera();

    public final Vector position = new Vector(-10, 0, 0);
    private final Vector viewPoint = new Vector(0, 0, 0);
    private final Vector up = new Vector(0, 1, 0);

    private Matrix rotation = Matrix.identity();

    public final Vector axisZ = viewPoint.copy()
            .shift(position.copy().resize(-1))
            .normalize();
    public final Vector axisX = axisZ.copy().
            crossProduct(up).
            normalize();
    public final Vector axisY = axisZ.copy().
            crossProduct(axisX).
            normalize();

    private double frontZ   = 5;
    private double backZ    = frontZ + 3;

    private double width    = 1;
    private double height   = 1;

    private Color color = Color.BLACK;

    private Camera() {}

    public static Camera getInstance() {
        return INSTANCE;
    }

//    ------   setters   ------

    public void rotate(double angleX, double angleY) {
        rotation.rotate(angleX, angleY, 0);
    }

    public void reset() {
        rotation = Matrix.identity();
    }

    public void move(double offset) {
        double moveZ = -offset * 0.05;

        if (frontZ + moveZ >= 1 && frontZ + moveZ <= 10 &&
                backZ + moveZ >= 1 && backZ + moveZ <= 10) {
            frontZ += moveZ;
            backZ += moveZ;
        }

        notifyObservers();
    }

    public void setRotation(Matrix rotation) {
        this.rotation = rotation;
    }

    public void setFrontZ(double frontZ) {
        this.frontZ = frontZ;

        notifyObservers();
    }

    public void setBackZ(double backZ) {
        this.backZ = backZ;

        notifyObservers();
    }

    public void setWidth(double width) {
        this.width = width;

        notifyObservers();
    }

    public void setHeight(double height) {
        this.height = height;

        notifyObservers();
    }

    public void setColor(Color color) {
        this.color = color;

        notifyObservers();
    }

    //    ------   getters   ------

    public Matrix getRotation() {
        return rotation;
    }

    public double getFrontZ() {
        return frontZ;
    }

    public double getBackZ() {
        return backZ;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    //    ------   util   ------


    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
