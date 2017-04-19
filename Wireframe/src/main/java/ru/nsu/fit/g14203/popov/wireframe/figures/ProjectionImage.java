package ru.nsu.fit.g14203.popov.wireframe.figures;

import javafx.geometry.Point2D;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

class ProjectionImage extends BufferedImage {

    private final static double FROM = -1;
    private final static double TO   = 1;

    private double scale;

    private Vector.Translation translation;
    private Vector.Projection projection;

    ProjectionImage(int size, Camera camera, Figure3D figure3D) {
        super(size, size, TYPE_INT_ARGB);

        translation = new Vector.Translation(camera.position, camera.axisX, camera.axisY, camera.axisZ);
        projection = new Vector.Projection(size, size, camera.getFrontZ(size), camera.getBackZ(size));

        scale = size / (TO - FROM);

        List<Figure3D.Edge> edges = figure3D.getEdges();
        for (Figure3D.Edge edge : edges) {
            Point2D[] edgeProjection = projectEdge(edge);
            if (edgeProjection == null)
                continue;

            int x1 = (int) Math.round((edgeProjection[0].getX() - FROM) * scale);
            int y1 = (int) Math.round((edgeProjection[0].getY() - FROM) * scale);
            int x2 = (int) Math.round((edgeProjection[1].getX() - FROM) * scale);
            int y2 = (int) Math.round((edgeProjection[1].getY() - FROM) * scale);

            Graphics2D g2D = createGraphics();
            g2D.setStroke(new BasicStroke(edge.lineWidth));
            g2D.setColor(edge.color);

            g2D.drawLine(x1, y1, x2, y2);
        }
    }

    private Point2D[] projectEdge(Figure3D.Edge edge) {
        Vector from = edge.points[0].copy()
                .translateTo(translation)
                .resize(scale * 1.25)
                .project(projection)
                .normalizeW();
        Vector to = edge.points[1].copy()
                .translateTo(translation)
                .resize(scale * 1.25)
                .project(projection)
                .normalizeW();

        double[] x = { from.getX(), to.getX() };
        double[] y = { from.getY(), to.getY() };
        double[] z = { from.getZ(), to.getZ() };

//        if (z[0] < 0 && z[1] < 0 || z[0] > 1 && z[1] > 1)
//            return null;
//
//        int min = (z[0] < z[1]) ? 0 : 1;
//        int max = 1 - min;
//
//        if (z[min] < 0) {
//            double k = (z[min] - 0) / (z[1] - z[0]);
//            x[min] = x[min] - k * (x[1] - x[0]);
//            y[min] = y[min] - k * (y[1] - y[0]);
//        }
//
//        if (z[max] > 1) {
//            double k = (z[max] - 1) / (z[1] - z[0]);
//            x[max] = x[max] - k * (x[1] - x[0]);
//            y[max] = y[max] - k * (y[1] - y[0]);
//        }

        return new Point2D[]{ new Point2D(x[0], y[0]), new Point2D(x[1], y[1]) };
    }
}
