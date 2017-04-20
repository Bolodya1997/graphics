package ru.nsu.fit.g14203.popov.wireframe.figures.matrix;

import javafx.geometry.Point3D;

public class Vector extends Matrix {

    public static class Translation {
        private Jama.Matrix matrix;

        public Translation(Vector center, Vector axisX, Vector axisY, Vector axisZ) {
            matrix = new Jama.Matrix(new double[][]{
                    { axisX.getX(), axisX.getY(), axisX.getZ(), 0 },
                    { axisY.getX(), axisY.getY(), axisY.getZ(), 0 },
                    { axisZ.getX(), axisZ.getY(), axisZ.getZ(), 0 },
                    { 0,            0,            0,            1 }
            });

            Jama.Matrix shift = Matrix.identity().shift(center.copy().resize(-1)).matrix;
            matrix = matrix.times(shift);
        }
    }

    public static class Projection {
        private Jama.Matrix matrix;

        public Projection(double width, double height, double frontZ, double backZ) {
            double depth = backZ - frontZ;

            matrix = new Jama.Matrix(new double[][]{
                    { 2 * frontZ / width, 0,                   0,              0                       },
                    { 0,                  2 * frontZ / height, 0,              0                       },
                    { 0,                  0,                   frontZ / depth, -frontZ * backZ / depth },
                    { 0,                  0,                   1,              0                       }
            });
        }
    }

    public static Vector zero() {
        return new Vector(0, 0, 0);
    }

    public Vector crossProduct(Vector other) {
        matrix = new Vector(getY() * other.getZ() - getZ() * other.getY(),
                            getZ() * other.getX() - getX() * other.getZ(),
                            getX() * other.getY() - getY() * other.getX()).matrix;

        return this;
    }

    public Vector(double x, double y, double z) {
        super(new double[][]{
                { x },
                { y },
                { z },
                { 1 }
        });
    }

    private Vector(Jama.Matrix matrix) {
        super(matrix);
    }

    public double getX() {
        return matrix.get(0, 0);
    }

    public double getY() {
        return matrix.get(1, 0);
    }

    public double getZ() {
        return matrix.get(2, 0);
    }

    public Vector copy() {
        return new Vector(matrix.copy());
    }

    public Vector normalizeW() {
        matrix = matrix.times(1 / matrix.get(3, 0));

        return this;
    }

    public Vector normalize() {
        double norm = matrix.getMatrix(0, 2, 0, 0).normF();

        return this.resize(1 / norm);
    }

    @Override
    public Matrix rotateX(double angle) {
        return super.rotateX(angle);
    }

    @Override
    public Matrix rotateY(double angle) {
        return super.rotateY(angle);
    }

    @Override
    public Matrix rotateZ(double angle) {
        return super.rotateZ(angle);
    }

    @Override
    public Vector rotate(double angleX, double angleY, double angleZ) {
        return (Vector) super.rotate(angleX, angleY, angleZ);
    }

    @Override
    public Vector rotate(Vector center, double angleX, double angleY, double angleZ) {
        return (Vector) super.rotate(center, angleX, angleY, angleZ);
    }

    @Override
    public Vector shift(Vector vector) {
        return (Vector) super.shift(vector);
    }

    @Override
    public Vector resize(double factor) {
        return (Vector) super.resize(factor);
    }

    public Vector translateTo(Translation translation) {
        matrix = translation.matrix.times(matrix);

        return this;
    }

    @Override
    public Matrix apply(Matrix other) {
        return super.apply(other);
    }

    public Vector translateFrom(Translation translation) {
        Jama.Matrix transposed = translation.matrix.transpose();
        matrix = transposed.times(matrix);

        return this;
    }

    public Vector project(Projection projection) {
        matrix = projection.matrix.times(matrix);

        return this;
    }
}
