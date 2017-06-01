package ru.nsu.fit.g14203.popov.raytracing.tracing;

import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.util.Objects;
import java.util.stream.Stream;

public class Box extends RealFigure3D {

    private final static Vector NORM_MIN_X = new Vector( 1,  0,  0);
    private final static Vector NORM_MAX_X = new Vector(-1,  0,  0);
    private final static Vector NORM_MIN_Y = new Vector( 0,  1,  0);
    private final static Vector NORM_MAX_Y = new Vector( 0, -1,  0);
    private final static Vector NORM_MIN_Z = new Vector( 0,  0,  1);
    private final static Vector NORM_MAX_Z = new Vector( 0,  0, -1);

    private Vector minPos;
    private Vector maxPos;

    public Box(float[] diffuseModifier, float[] specularModifier, float shiny,
               Vector minPos, Vector maxPos) {
        super(diffuseModifier, specularModifier, shiny);

        this.minPos = minPos;
        this.maxPos = maxPos;

        Vector p001 = new Vector(minPos.getX(), minPos.getY(), maxPos.getZ());
        Vector p010 = new Vector(minPos.getX(), maxPos.getY(), minPos.getZ());
        Vector p011 = new Vector(minPos.getX(), maxPos.getY(), maxPos.getZ());
        Vector p100 = new Vector(maxPos.getX(), minPos.getY(), minPos.getZ());
        Vector p101 = new Vector(maxPos.getX(), minPos.getY(), maxPos.getZ());
        Vector p110 = new Vector(maxPos.getX(), maxPos.getY(), minPos.getZ());

        addEdge(new Edge(minPos, p001));
        addEdge(new Edge(minPos, p010));
        addEdge(new Edge(minPos, p100));
        addEdge(new Edge(p001, p011));
        addEdge(new Edge(p001, p101));
        addEdge(new Edge(p010, p011));
        addEdge(new Edge(p010, p110));
        addEdge(new Edge(p011, maxPos));
        addEdge(new Edge(p100, p101));
        addEdge(new Edge(p100, p110));
        addEdge(new Edge(p101, maxPos));
        addEdge(new Edge(p110, maxPos));
    }

    @Override
    public Vector[] getMinMaxPoints() {
        return new Vector[]{ minPos, maxPos };
    }

    @Override
    public ContactPoint contact(Ray reflected) {
        return Stream.<ContactPoint>builder()
                .add(getPoint(reflected, NORM_MIN_X,  minPos.getX()))
                .add(getPoint(reflected, NORM_MAX_X, -maxPos.getX()))
                .add(getPoint(reflected, NORM_MIN_Y,  minPos.getY()))
                .add(getPoint(reflected, NORM_MAX_Y, -maxPos.getY()))
                .add(getPoint(reflected, NORM_MIN_Z,  minPos.getZ()))
                .add(getPoint(reflected, NORM_MAX_Z, -maxPos.getZ()))
                .build()
                .filter(Objects::nonNull)
                .filter(cPoint -> cPoint.pos.getX() >= minPos.getX())
                .filter(cPoint -> cPoint.pos.getX() <= maxPos.getX())
                .filter(cPoint -> cPoint.pos.getY() >= minPos.getY())
                .filter(cPoint -> cPoint.pos.getY() <= maxPos.getY())
                .filter(cPoint -> cPoint.pos.getZ() >= minPos.getZ())
                .filter(cPoint -> cPoint.pos.getZ() <= maxPos.getZ())
                .findFirst().orElse(null);
    }

    private ContactPoint getPoint(Ray reflected, Vector norm, double dist) {
        double vD = norm.dotProduct(reflected.direction);
        if (vD <= 0)
            return null;

        double v0 = dist - norm.dotProduct(reflected.from);
        double t = v0 / vD + 0.000001;
        if (t < 0)
            return null;

        Vector point = reflected.from.copy()
                .shift(reflected.direction.copy()
                        .resize(t));

        return new ContactPoint(point, norm, reflected);
    }
}
