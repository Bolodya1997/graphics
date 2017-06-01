package ru.nsu.fit.g14203.popov.raytracing.tracing;

import ru.nsu.fit.g14203.popov.wireframe.Camera;
import ru.nsu.fit.g14203.popov.wireframe.matrix.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;

public class RenderImage extends BufferedImage {

    private final static int DEPTH = 10;

    private Collection<RealFigure3D> figures;
    private Collection<Light> lights;

    private float[] ambientIntense;

    private float[][][] pixelIntenses;  //  [x][y]{ r, g, b }
    private float maxIntense;

    public RenderImage(int width, int height, Camera camera,
                       Collection<RealFigure3D> figures, Collection<Light> lights,
                       float[] ambientIntense) {
        super(width, height, TYPE_INT_RGB);

        this.figures = figures;
        this.lights = lights;
        this.ambientIntense = ambientIntense;

        pixelIntenses = new float[width][height][3];

        double dx = camera.getWidth() / width;
        double dy = camera.getHeight() / height;

        Vector from         = camera.getPosition();
        Vector toDirection  = from.copy().resize(-1);

        Vector.Translation translation = new Vector.Translation(from,
                camera.getAxisX(), camera.getAxisY(), camera.getAxisZ());

        double x = -camera.getWidth() / 2;
        for (int i = 0; i < width; i++, x += dx) {

            double y = -camera.getHeight() / 2;
            for (int j = 0; j < height; j++, y += dy) {
                Vector direction = new Vector(x, y, camera.getFrontZ())
                        .translateFrom(translation)
                        .shift(toDirection)
                        .normalize();
                computePixel(i, j, new Ray(from, direction));
            }

            System.out.println(i * 100.0 / width + "%");
        }

        float intenseModifier = 1 / maxIntense;
        for (int X = 0; X < width; X++) {
            for (int Y = 0; Y < height; Y++) {
                Color color = new Color(
                        pixelIntenses[X][Y][0] * intenseModifier,
                        pixelIntenses[X][Y][1] * intenseModifier,
                        pixelIntenses[X][Y][2] * intenseModifier
                );
                setRGB(X, Y, color.getRGB());
            }
        }
    }

    private void computePixel(int x, int y, Ray ray) {
        LinkedList<RealFigure3D.ContactPoint> stack = new LinkedList<>();

        Ray[] eye = { ray };
        for (int i = 0; i < DEPTH; i++) {
            RealFigure3D.ContactPoint contactPoint = figures.stream()
                    .map(figure -> figure.contact(eye[0]))
                    .filter(Objects::nonNull)
                    .min(Comparator.comparingDouble(cPoint ->
                            cPoint.pos.distance(eye[0].from))).orElse(null);
            stack.push(contactPoint);

            if (contactPoint == null)
                break;
            eye[0] = contactPoint.income;
        }

        RealFigure3D.ContactPoint last = stack.peek();
        if (last == null) {
            stack.pop();
            if (!stack.isEmpty())
                stack.peek().income.intense = ambientIntense;
            else
                ray.intense = ambientIntense;
        }

        while (!stack.isEmpty()) {
            RealFigure3D.ContactPoint contactPoint = stack.pop();
            contactPoint.getFigure().setReflectedIntense(lights, figures, ambientIntense, contactPoint);
        }

        pixelIntenses[x][y] = ray.intense;
        maxIntense = Math.max(Math.max(maxIntense, ray.intense[0]),
                              Math.max(ray.intense[1], ray.intense[2]));
    }
}
