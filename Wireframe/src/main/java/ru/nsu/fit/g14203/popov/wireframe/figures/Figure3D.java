package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Figure3D {

    protected static class Edge {
        private Vector[] points = new Vector[2];
        private Color color = Color.WHITE;

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

    private Edge[] axises = new Edge[3];
    private ArrayList<Edge> edges = new ArrayList<>();

    public static Figure3D getBrick(double sizeX, double sizeY, double sizeZ) {
        Vector p000 = new Vector( sizeX,  sizeY,  sizeZ);
        Vector p001 = new Vector( sizeX,  sizeY, -sizeZ);
        Vector p010 = new Vector( sizeX, -sizeY,  sizeZ);
        Vector p011 = new Vector( sizeX, -sizeY, -sizeZ);
        Vector p100 = new Vector(-sizeX,  sizeY,  sizeZ);
        Vector p101 = new Vector(-sizeX,  sizeY, -sizeZ);
        Vector p110 = new Vector(-sizeX, -sizeY,  sizeZ);
        Vector p111 = new Vector(-sizeX, -sizeY, -sizeZ);

        return new Figure3D()
                .addEdge(new Edge(p000, p001))
                .addEdge(new Edge(p000, p010))
                .addEdge(new Edge(p000, p100))
                .addEdge(new Edge(p001, p011))
                .addEdge(new Edge(p001, p101))
                .addEdge(new Edge(p010, p011))
                .addEdge(new Edge(p010, p110))
                .addEdge(new Edge(p011, p111))
                .addEdge(new Edge(p100, p101))
                .addEdge(new Edge(p100, p110))
                .addEdge(new Edge(p101, p111))
                .addEdge(new Edge(p110, p111));
    }

    protected Figure3D() {
        axises[0] = new Figure3D.Edge(
                Vector.zero(),
                new Vector(0.2, 0,0), Color.RED);

        axises[1] = new Figure3D.Edge(
                Vector.zero(),
                new Vector(0, 0.2, 0), Color.GREEN);

        axises[2] = new Figure3D.Edge(
                Vector.zero(),
                new Vector(0, 0, 0.2), Color.BLUE);
    }

    protected Figure3D addEdge(Edge edge) {
        Edge tmp = new Edge(
                edge.points[0].copy(),
                edge.points[1].copy(), edge.color);
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

    List<Edge> getEdges(Matrix toScene, Camera camera) {
        Vector.Translation translation = new Vector.Translation(Vector.zero(),
                camera.axisX, camera.axisY, camera.axisZ);

        return Stream.concat(Arrays.stream(axises), edges.stream())
                .map(edge -> {
                    Vector[] points = Arrays.stream(edge.points)
                            .map(v -> v.copy()
                                    .apply(rotation)
                                    .shift(center)
                                    .apply(toScene)
                                    .translateTo(translation)
                                    .apply(camera.getRotation())
                                    .translateFrom(translation))
                            .toArray(Vector[]::new);

                    return new Edge(points[0], points[1], edge.color);
                })
                .collect(Collectors.toList());
    }

    public Matrix getRotation() {
        return rotation;
    }

    public Vector getCenter() {
        return center;
    }
}
