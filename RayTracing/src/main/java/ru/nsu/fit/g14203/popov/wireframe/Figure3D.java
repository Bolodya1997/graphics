package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Figure3D {

    protected static class Edge {
        private Vector[] points = new Vector[2];
        private Color color = Color.BLACK;

        public Edge(Vector p1, Vector p2, Color color) {
            points[0] = p1;
            points[1] = p2;

            this.color = color;
        }

        public Edge(Vector p1, Vector p2) {
            points[0] = p1;
            points[1] = p2;
        }

        public Vector[] getPoints() {
            return points;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    private Matrix rotation = Matrix.identity();
    private Vector center = Vector.zero();

    private ArrayList<Edge> edges = new ArrayList<>();

    protected Figure3D addEdge(Edge edge) {
        Vector[] points = Arrays.stream(edge.points)
                .map(v -> v.copy()
                        .apply(rotation)
                        .shift(center))
                .toArray(Vector[]::new);

        Edge tmp = new Edge(points[0], points[1], edge.color);
        edges.add(tmp);

        return this;
    }

    protected void clear() {
        edges.clear();
    }

//    ------   moving   ------

    public void rotate(double angleX, double angleY, double angleZ) {
        Matrix matrix = Matrix.identity().rotate(angleX, angleY, angleZ);
        rotate(matrix);
    }

    public void rotate(Matrix matrix) {
        rotation.apply(matrix);
    }

    public void shift(Vector vector) {
        center.shift(vector);
    }

//    ------   get data   ------

    public Vector[] getMinMaxPoints() {
        double[] minMaxX = { center.getX(), center.getX() };
        double[] minMaxY = { center.getY(), center.getY() };
        double[] minMaxZ = { center.getZ(), center.getZ() };

        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.getPoints()))
                .forEach(vector -> {
                    Vector __vector = vector.copy()
                            .apply(rotation)
                            .shift(center);

                    minMaxX[0] = Double.min(__vector.getX(), minMaxX[0]);
                    minMaxX[1] = Double.max(__vector.getX(), minMaxX[1]);

                    minMaxY[0] = Double.min(__vector.getY(), minMaxY[0]);
                    minMaxY[1] = Double.max(__vector.getY(), minMaxY[1]);

                    minMaxZ[0] = Double.min(__vector.getZ(), minMaxZ[0]);
                    minMaxZ[1] = Double.max(__vector.getZ(), minMaxZ[1]);
                });

        return new Vector[]{
                new Vector(minMaxX[0], minMaxY[0], minMaxZ[0]),
                new Vector(minMaxX[1], minMaxY[1], minMaxZ[1])
        };
    }

    public List<Edge> getEdges() {
        return edges.stream()
                .map(edge -> {
                    Vector[] points = Arrays.stream(edge.points)
                            .map(Vector::copy)
                            .toArray(Vector[]::new);

                    return new Edge(points[0], points[1], edge.color);
                })
                .collect(Collectors.toList());
    }
}
