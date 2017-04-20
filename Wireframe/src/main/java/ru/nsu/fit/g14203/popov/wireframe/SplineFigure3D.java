package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.stream.Stream;

class SplineFigure3D extends Figure3D {

    private final static double ROTATION = 10;

    SplineFigure3D(Spline spline) {
        double angleZ = 2 * Math.PI / ROTATION;

        Stream.Builder<Edge> __edges = Stream.builder();
        Point2D last = Arrays.stream(spline.getPoints())
                .reduce((p1, p2) -> {
                    Edge tmp = new Edge(new Vector[]{
                            new Vector(0, p1.getY(), p1.getX()),
                            new Vector(0, p2.getY(), p2.getX()) });
                    tmp.setColor(spline.getColor());
                    __edges.add(tmp);

                    tmp = new Edge(new Vector[]{
                            new Vector(0, p1.getY(), p1.getX()),
                            new Vector(0, p1.getY(), p1.getX()).rotate(0, 0, angleZ)
                    });
                    tmp.setColor(spline.getColor());
                    __edges.add(tmp);

                    return p2;
                }).orElse(null);

        if (last != null) {
            Edge tmp = new Edge(new Vector[]{
                    new Vector(0, last.getY(), last.getX()),
                    new Vector(0, last.getY(), last.getX()).rotate(0, 0, angleZ)
            });
            tmp.setColor(spline.getColor());
            __edges.add(tmp);
        }

        Edge[] edges = __edges.build().toArray(Edge[]::new);

        for (int i = 0; i < ROTATION; i++) {
            for (Edge edge : edges)
                addEdge(edge);

            rotate(0, 0, angleZ);
        }

        rotate(Math.PI / 4, 0, 0);
        shift(new Vector(0.2, -0.3, 0.6));
    }
}
