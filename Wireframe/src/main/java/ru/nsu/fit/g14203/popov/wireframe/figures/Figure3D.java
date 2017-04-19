package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Figure3D {

    static class Edge {
        Vector[] points;

        Color color = Color.WHITE;
        int lineWidth = 1;

        Edge(Vector[] points) {
            this.points = points;
        }
    }

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
                .addEdge(new Edge(new Vector[]{ p000.copy(), p001.copy() }))
                .addEdge(new Edge(new Vector[]{ p000.copy(), p010.copy() }))
                .addEdge(new Edge(new Vector[]{ p000.copy(), p100.copy() }))
                .addEdge(new Edge(new Vector[]{ p001.copy(), p011.copy() }))
                .addEdge(new Edge(new Vector[]{ p001.copy(), p101.copy() }))
                .addEdge(new Edge(new Vector[]{ p010.copy(), p011.copy() }))
                .addEdge(new Edge(new Vector[]{ p010.copy(), p110.copy() }))
                .addEdge(new Edge(new Vector[]{ p011.copy(), p111.copy() }))
                .addEdge(new Edge(new Vector[]{ p100.copy(), p101.copy() }))
                .addEdge(new Edge(new Vector[]{ p100.copy(), p110.copy() }))
                .addEdge(new Edge(new Vector[]{ p101.copy(), p111.copy() }))
                .addEdge(new Edge(new Vector[]{ p110.copy(), p111.copy() }));
    }

    protected Figure3D addEdge(Edge edge) {
        edges.add(edge);
        return this;
    }

    protected void rotate(Vector center, double angleX, double angleY, double angleZ) {
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.rotate(center, angleX, angleY, angleZ));
    }

    protected void shift(Vector vector) {
        edges.stream()
                .flatMap(edge -> Arrays.stream(edge.points))
                .forEach(v -> v.shift(vector));
    }

    void setEdgesColor(Color color) {
        for (Edge edge : edges)
            edge.color = color;
    }

    void setEdgesLineWidth(int lineWidth) {
        for (Edge edge : edges)
            edge.lineWidth = lineWidth;
    }

    void rotateCamera(Camera camera, double angleX, double angleY) {
        Vector.Translation translation = new Vector.Translation(Vector.ZERO,
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
