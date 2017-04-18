package ru.nsu.fit.g14203.popov.wireframe.matrix;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

class Matrix {

    Jama.Matrix matrix;

    Matrix(double[][] A) {
        matrix = new Jama.Matrix(A);
    }

    Matrix(double[] vals, int m) {
        matrix = new Jama.Matrix(vals, m);
    }

    Matrix(Jama.Matrix matrix) {
        this.matrix = matrix;
    }

    public Matrix rotateX(double angle) {
        Jama.Matrix rotate = new Jama.Matrix(new double[][]{
                {  cos(angle),  0,           sin(angle), 0 },
                {  0,           1,           0,          0 },
                { -sin(angle),  0,           cos(angle), 0 },
                {  0,           0,           0,          1 } });
        matrix = rotate.times(matrix);

        return this;
    }

    public Matrix rotateY(double angle) {
        Jama.Matrix rotate = new Jama.Matrix(new double[][]{
                { 1,            0,           0,          0 },
                { 0,            cos(angle), -sin(angle), 0 },
                { 0,            sin(angle),  cos(angle), 0 },
                { 0,            0,           0,          1 } });
        matrix = rotate.times(matrix);

        return this;
    }

    public Matrix rotateZ(double angle) {
        Jama.Matrix rotate = new Jama.Matrix(new double[][]{
                {  cos(angle), -sin(angle),  0,          0 },
                {  sin(angle),  cos(angle),  0,          0 },
                {  0,            0,          1,          0 },
                {  0,            0,          0,          1 } });
        matrix = rotate.times(matrix);

        return this;
    }

    public Matrix rotate(double angleX, double angleY, double angleZ) {
        rotateX(angleX);
        rotateY(angleY);
        rotateZ(angleZ);

        return this;
    }

    public Matrix rotate(Vector center, double angleX, double angleY, double angleZ) {
        shift(center.copy().resize(-1));    //  center -> (0, 0, 0)
        rotate(angleX, angleY, angleZ);
        shift(center);                      //  (0, 0, 0) -> center

        return this;
    }

    public Matrix shift(Vector vector) {
        Jama.Matrix shift = new Jama.Matrix(new double[][]{
                { 1, 0, 0, vector.getX() },
                { 0, 1, 0, vector.getY() },
                { 0, 0, 1, vector.getZ() },
                { 0, 0, 0, 1             } });
        matrix = shift.times(matrix);

        return this;
    }

    public Matrix resize(double factor) {
        Jama.Matrix resize = new Jama.Matrix(new double[][]{
                { factor, 0,      0,      0 },
                { 0,      factor, 0,      0 },
                { 0,      0,      factor, 0 },
                { 0,      0,      0,      1 }
        });
        matrix = resize.times(matrix);

        return this;
    }
}
