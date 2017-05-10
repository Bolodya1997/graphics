package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineOwner;

import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Stream;

class SplineFigure3D extends Figure3D implements Observer {

    private SplineOwner splineOwner = SplineOwner.getInstance();

    private Spline spline;

    SplineFigure3D(Spline spline) {
        this.spline = spline;
        recount();
    }

    private void recount() {
        clear();

        Stream.Builder<Edge> __edges = Stream.builder();
        Arrays.stream(spline.getSpline())
                .reduce((p1, p2) -> {
                    Edge tmp = new Edge(
                            new Vector(p1.getX(), -p1.getY(), 0)
                                    .rotate(0, 0, splineOwner.getRotateFrom()),
                            new Vector(p2.getX(), -p2.getY(), 0)
                                    .rotate(0, 0, splineOwner.getRotateFrom()),
                            spline.getColor());
                    __edges.add(tmp);

                    return p2;
                });
        Edge[] edges = __edges.build().toArray(Edge[]::new);

        double angleStep = (splineOwner.getRotateTo() - splineOwner.getRotateFrom()) /
                splineOwner.getRotateCount();

        for (int i = 0; i < edges.length; i++) {
            Edge prev = edges[i];
            for (int j = 0; j < splineOwner.getRotateCount() + 1; j++) {
                Edge cur = new Edge(
                        edges[i].getPoints()[0].copy()
                                .rotate(j * angleStep, 0, 0),
                        edges[i].getPoints()[1].copy()
                                .rotate(j * angleStep, 0, 0),
                        spline.getColor());
                addEdge(cur);

                Edge edge = new Edge(
                        prev.getPoints()[0],
                        cur.getPoints()[0],
                        spline.getColor());
                addEdge(edge);

                if (i == edges.length - 1) {
                    Edge last = new Edge(
                            prev.getPoints()[1],
                            cur.getPoints()[1],
                            spline.getColor());
                    addEdge(last);
                }

                prev = cur;
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        recount();
    }
}
