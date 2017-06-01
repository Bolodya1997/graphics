package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.util.Observable;

public class Camera extends Observable {

    private Vector position;
    private Vector viewPoint;
    private Vector up;

    private Vector axisX;
    private Vector axisY;
    private Vector axisZ;

    private double frontZ   = 1;
    private double backZ    = frontZ + 3;

    private double width    = 1;
    private double height   = 1;

    public Camera(Vector position, Vector viewPoint, Vector up) {
        setBaseVectors(position, viewPoint, up);
    }

    public Camera() {
        position    = new Vector(0.5, 1.1, -0.5);
        viewPoint   = new Vector(-0.3, 0.6, 0.3);
        up          = new Vector(0, 1, 0);

        setBaseVectors(position, viewPoint, up);
    }

//    ------   setters   ------

    public void setBaseVectors(Vector position, Vector viewPoint, Vector up) {
        this.position   = position;
        this.viewPoint  = viewPoint;
        this.up         = up;

        axisZ = viewPoint.copy()
                .shift(position.copy().resize(-1))
                .normalize();
        axisX = axisZ.copy().
                crossProduct(up).
                normalize();
        axisY = axisZ.copy().
                crossProduct(axisX).
                normalize();
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

    //    ------   getters   ------


    public Vector getPosition() {
        return position;
    }

    public Vector getViewPoint() {
        return viewPoint;
    }

    public Vector getUp() {
        return up;
    }

    public Vector getAxisX() {
        return axisX;
    }

    public Vector getAxisY() {
        return axisY;
    }

    public Vector getAxisZ() {
        return axisZ;
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

    //    ------   util   ------

    @Override
    public void notifyObservers(Object arg) {
        setChanged();
        super.notifyObservers(arg);
    }
}
