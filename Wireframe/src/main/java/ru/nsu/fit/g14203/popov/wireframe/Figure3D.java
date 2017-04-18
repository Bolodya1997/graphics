package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.awt.*;
import java.util.stream.Stream;

class Figure3D {

    static class Edge {
        Vector[] points;

        Color color = Color.WHITE;
        int lineWidth = 1;

        Edge(Vector[] points) {
            this.points = points;
        }
    }

    private Edge[] edges;
    private Stream.Builder<Edge> __edges = Stream.builder();

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
                .addEdge(new Edge(new Vector[]{ p110.copy(), p111.copy() }))
                .build();
    }

    Figure3D addEdge(Edge edge) {
        __edges.add(edge);
        return this;
    }

    Figure3D build() {
        edges = __edges.build().toArray(Edge[]::new);
        return this;
    }

    void rotate(double angleX, double angleY, double angleZ) {
        for (Edge edge : edges) {
            for (int i = 0; i < 2; i++)
                edge.points[i].rotate(angleX, angleY, angleZ);
        }
    }

    void shift(Vector vector) {
        for (Edge edge : edges) {
            for (int i = 0; i < 2; i++)
                edge.points[i].shift(vector);
        }
    }

    void setEdgesColor(Color color) {
        for (Edge edge : edges)
            edge.color = color;
    }

    void setEdgesLineWidth(int lineWidth) {
        for (Edge edge : edges)
            edge.lineWidth = lineWidth;
    }

    Edge[] getEdges() {
        return edges;
    }
}
