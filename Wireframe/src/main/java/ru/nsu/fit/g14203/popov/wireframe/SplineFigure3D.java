package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.util.Sequence;
import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineOwner;

import java.awt.geom.Point2D;
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

        Sequence baseSequence = new Sequence(splineOwner.getLengthFrom(), splineOwner.getLengthTo(),
                splineOwner.getLengthCount());
        Stream.generate(baseSequence::next)
                .limit(splineOwner.getLengthCount())
                .reduce((l1, l2) -> {
                    Point2D first = spline.getPointAtLength(l1);
                    rotateVector(new Vector(first.getX(), -first.getY(), 0));

                    Sequence offSequence = new Sequence(l1, l2, splineOwner.getLengthK());
                    Stream.generate(offSequence::next)
                            .limit(splineOwner.getLengthK())
                            .map(length -> spline.getPointAtLength(length))
                            .map(point2D -> new Vector(point2D.getX(), -point2D.getY(), 0))
                            .reduce((v1, v2) -> {
                                rotateEdge(new Edge(v1, v2, spline.getColor()));
                                return v2;
                            });

                    return l2;
                });

        Point2D last = spline.getPointAtLength(splineOwner.getLengthTo());
        rotateVector(new Vector(last.getX(), -last.getY(), 0));
    }

    private void rotateVector(Vector vector) {
        Sequence rotateSequence = new Sequence(splineOwner.getRotateFrom(), splineOwner.getRotateTo(),
                splineOwner.getRotateCount() + 1);
        Stream.generate(rotateSequence::next)
                .limit(splineOwner.getRotateCount() + 1)
                .map(angle -> vector.copy().rotate(angle, 0, 0))
                .reduce((v1, v2) -> {
                    addEdge(new Edge(v1, v2, spline.getColor()));
                    return v2;
                });
    }

    private void rotateEdge(Edge edge) {
        Sequence rotateSequence = new Sequence(splineOwner.getRotateFrom(), splineOwner.getRotateTo(),
                splineOwner.getRotateCount() + 1);
        Stream.generate(rotateSequence::next)
                .limit(splineOwner.getRotateCount() + 1)
                .map(angle -> {
                    Vector p1 = edge.getPoints()[0].copy().rotate(angle, 0, 0);
                    Vector p2 = edge.getPoints()[1].copy().rotate(angle, 0, 0);

                    return new Edge(p1, p2, spline.getColor());
                })
                .forEach(this::addEdge);
    }

    @Override
    public void update(Observable o, Object arg) {
        recount();
    }
}
