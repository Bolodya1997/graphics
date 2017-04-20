package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Figure3D {

    protected static class Edge {
        Vector[] points;
        Color color = Color.WHITE;

        public Edge(Vector[] points) {
            this.points = points;
        }

        public void setColor(Color color) {
            this.color = color;
        }
    }

    private Matrix rotation = Matrix.identity();
    private Vector center = Vector.zero();

    private ArrayList<Edge> edges = new ArrayList<>();

    static Figure3D getBrick(double sizeX, double sizeY, double sizeZ) {
        Vector p000 = new Vector( sizeX,  sizeY,  sizeZ);
        Vector p001 = new Vector( sizeX,  sizeY, -sizeZ);
        Vector p010 = new Vector( sizeX, -sizeY,  sizeZ);
        Vector p011 = new Vector( sizeX, -sizeY, -sizeZ);
        Vector p100 = new Vector(-sizeX,  sizeY,  sizeZ);
        Vector p101 = new Vector(-sizeX,  sizeY, -sizeZ);
        Vector p110 = new Vector(-sizeX, -sizeY,  sizeZ);
        Vector p111 = new Vector(-sizeX, -sizeY, -sizeZ);

        return new Figure3D()
                .addEdge(new Edge(new Vector[]{ p000, p001 }))
                .addEdge(new Edge(new Vector[]{ p000, p010 }))
                .addEdge(new Edge(new Vector[]{ p000, p100 }))
                .addEdge(new Edge(new Vector[]{ p001, p011 }))
                .addEdge(new Edge(new Vector[]{ p001, p101 }))
                .addEdge(new Edge(new Vector[]{ p010, p011 }))
                .addEdge(new Edge(new Vector[]{ p010, p110 }))
                .addEdge(new Edge(new Vector[]{ p011, p111 }))
                .addEdge(new Edge(new Vector[]{ p100, p101 }))
                .addEdge(new Edge(new Vector[]{ p100, p110 }))
                .addEdge(new Edge(new Vector[]{ p101, p111 }))
                .addEdge(new Edge(new Vector[]{ p110, p111 }));
    }

    protected Figure3D() {
        Figure3D.Edge axisX = new Figure3D.Edge(new Vector[]{ new Vector(0, 0, 0),
                new Vector(0.2, 0,0)});
        axisX.color = Color.RED;
        addEdge(axisX);

        Figure3D.Edge axisY = new Figure3D.Edge(new Vector[]{ new Vector(0, 0, 0),
                new Vector(0, 0.2, 0)});
        axisY.color = Color.GREEN;
        addEdge(axisY);

        Figure3D.Edge axisZ = new Figure3D.Edge(new Vector[]{ new Vector(0, 0, 0),
                new Vector(0, 0, 0.2)});
        axisZ.color = Color.BLUE;
        addEdge(axisZ);
    }

    protected Figure3D addEdge(Edge edge) {
        Edge tmp = new Edge(new Vector[]{
                edge.points[0].copy(),
                edge.points[1].copy()
        });
        tmp.color = edge.color;

        edges.add(tmp);

        return this;
    }

    protected void rotate(double angleX, double angleY, double angleZ) {
        Matrix matrix = Matrix.identity().rotate(center, angleX, angleY, angleZ);

        rotation.apply(matrix);
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.apply(matrix));
    }

    protected void shift(Vector vector) {
        center.shift(vector);
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.shift(vector));
    }

    protected void rotate(Matrix matrix) {
        rotation.apply(matrix);
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.apply(matrix));
    }

    void setEdgesColor(Color color) {
        edges.stream()
                .skip(3)
                .forEach(edge -> edge.color = color);
    }

    void rotateCamera(Camera camera, double angleX, double angleY) {
        Vector.Translation translation = new Vector.Translation(Vector.zero(),
                                                                camera.axisX, camera.axisY, camera.axisZ);
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.translateTo(translation)
                        .rotate(angleX, angleY, 0)
                        .translateFrom(translation));
    }

    List<Edge> getEdges() {
        return edges;
    }
}
