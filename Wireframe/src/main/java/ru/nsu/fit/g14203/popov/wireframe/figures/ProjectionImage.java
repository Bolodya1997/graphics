package ru.nsu.fit.g14203.popov.wireframe.figures;

import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;

import java.awt.*;
import java.awt.geom.Point2D;
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
            Point2D.Double[] edgeProjection = projectEdge(edge);
            if (edgeProjection == null)
                continue;

            int x1 = (int) Math.round((edgeProjection[0].getX() - FROM) * scale);
            int y1 = (int) Math.round((edgeProjection[0].getY() - FROM) * scale);
            int x2 = (int) Math.round((edgeProjection[1].getX() - FROM) * scale);
            int y2 = (int) Math.round((edgeProjection[1].getY() - FROM) * scale);

            Graphics2D g2D = createGraphics();
            g2D.setColor(edge.color);

            g2D.drawLine(x1, y1, x2, y2);
        }
    }

    private Point2D.Double[] projectEdge(Figure3D.Edge edge) {
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

        try {
            clipping(x, y, z, FROM, TO);
            clipping(y, x, z, FROM, TO);
            clipping(z, x, y, 0, 1);
        } catch (Exception e) {
            return null;
        }

        return new Point2D.Double[]{ new Point2D.Double(x[0], y[0]), new Point2D.Double(x[1], y[1]) };
    }

    private void clipping(double[] main, double[] off1, double[] off2,
                          double min, double max) throws Exception {
        if (main[0] < min && main[1] < min || main[0] > max && main[1] > max) {
            throw new Exception();
        }

        int iMin = (main[0] < main[1]) ? 0 : 1;
        int iMax = 1 - iMin;

        if (main[iMin] < min) {
            double k = (main[iMin] - min) / (main[1] - main[0]);
            off1[iMin] = off1[iMin] - k * (off1[1] - off1[0]);
            off2[iMin] = off2[iMin] - k * (off2[1] - off2[0]);
        }

        if (main[iMax] > max) {
            double k = (main[iMax] - max) / (main[1] - main[0]);
            off1[iMax] = off1[iMax] - k * (off1[1] - off1[0]);
            off2[iMax] = off2[iMax] - k * (off2[1] - off2[0]);
        }
    }
}
