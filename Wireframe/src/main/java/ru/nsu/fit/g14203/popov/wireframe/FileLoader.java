package ru.nsu.fit.g14203.popov.wireframe;

import ru.nsu.fit.g14203.popov.wireframe.figures.Camera;
import ru.nsu.fit.g14203.popov.wireframe.figures.Figure3D;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Matrix;
import ru.nsu.fit.g14203.popov.wireframe.figures.matrix.Vector;
import ru.nsu.fit.g14203.popov.wireframe.spline.Spline;
import ru.nsu.fit.g14203.popov.wireframe.spline.SplineOwner;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileLoader {

    private static SplineOwner splineOwner = SplineOwner.getInstance();
    private static Camera camera = Camera.getInstance();

    private static String nextLine(Scanner scanner) {
        String line = "";
        while (line.isEmpty())
            line = scanner.nextLine().replaceAll("//.*", "");

        return line;
    }

    public static void load(InputStream stream, Map<Spline, SplineFigure3D> figures) {
        Scanner scanner = new Scanner(stream);

        double[] nmkabcd = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToDouble(Double::parseDouble)
                .toArray();
        splineOwner.setLengthCount((int) nmkabcd[0]);
        splineOwner.setRotateCount((int) nmkabcd[1]);
        splineOwner.setLengthK((int) nmkabcd[2]);
        splineOwner.setLengthFrom(nmkabcd[3]);
        splineOwner.setLengthTo(nmkabcd[4]);
        splineOwner.setRotateFrom(nmkabcd[5]);
        splineOwner.setRotateTo(nmkabcd[6]);

        double[] zFzBsWsH = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToDouble(Double::parseDouble)
                .toArray();
        camera.setFrontZ(zFzBsWsH[0]);
        camera.setBackZ(zFzBsWsH[1]);
        camera.setWidth(zFzBsWsH[2]);
        camera.setHeight(zFzBsWsH[3]);

        double[][] tmp = new double[3][];
        tmp[0] = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToDouble(Double::parseDouble)
                .toArray();
        tmp[1] = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToDouble(Double::parseDouble)
                .toArray();
        tmp[2] = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToDouble(Double::parseDouble)
                .toArray();
        Matrix cameraRotation = new Matrix(new double[][]{
                { tmp[0][0], tmp[0][1], tmp[0][2], 0 },
                { tmp[1][0], tmp[1][1], tmp[1][2], 0 },
                { tmp[2][0], tmp[2][1], tmp[2][2], 0 },
                { 0,         0,         0,         1 }
        });
        camera.setRotation(cameraRotation);

        int[] backRGB = Stream.of(nextLine(scanner))
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .flatMap(s -> Arrays.stream(s.split("\t")))
                .filter(s -> !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();
        camera.setColor(new Color(backRGB[0], backRGB[1], backRGB[2]));

        splineOwner.clear();

        int K = Integer.parseInt(nextLine(scanner));
        for (int i = 0; i < K; i++) {
            Spline spline = Spline.getEmptySpline();

            int[] RGB = Stream.of(nextLine(scanner))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .flatMap(s -> Arrays.stream(s.split("\t")))
                    .filter(s -> !s.isEmpty())
                    .mapToInt(Integer::parseInt)
                    .toArray();
            spline.setColor(new Color(RGB[0], RGB[1], RGB[2]));

            double cXYZ[] = Stream.of(nextLine(scanner))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .flatMap(s -> Arrays.stream(s.split("\t")))
                    .filter(s -> !s.isEmpty())
                    .mapToDouble(Double::parseDouble)
                    .toArray();

            tmp[0] = Stream.of(nextLine(scanner))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .flatMap(s -> Arrays.stream(s.split("\t")))
                    .filter(s -> !s.isEmpty())
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            tmp[1] = Stream.of(nextLine(scanner))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .flatMap(s -> Arrays.stream(s.split("\t")))
                    .filter(s -> !s.isEmpty())
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            tmp[2] = Stream.of(nextLine(scanner))
                    .flatMap(s -> Arrays.stream(s.split(" ")))
                    .flatMap(s -> Arrays.stream(s.split("\t")))
                    .filter(s -> !s.isEmpty())
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            Matrix rotation = new Matrix(new double[][]{
                    { tmp[0][0], tmp[0][1], tmp[0][2], 0 },
                    { tmp[1][0], tmp[1][1], tmp[1][2], 0 },
                    { tmp[2][0], tmp[2][1], tmp[2][2], 0 },
                    { 0,         0,         0,         1 }
            });

            int N = Integer.parseInt(nextLine(scanner));
            for (int j = 0; j < N; j++) {
                double[] xy = Stream.of(nextLine(scanner))
                        .flatMap(s -> Arrays.stream(s.split(" ")))
                        .flatMap(s -> Arrays.stream(s.split("\t")))
                        .filter(s -> !s.isEmpty())
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                spline.addPoint(new Point2D.Double(xy[0], xy[1]));
            }

            splineOwner.addSpline(spline);

            Figure3D figure = figures.get(spline);
            figure.shift(new Vector(cXYZ[0], cXYZ[1], cXYZ[2]));
            figure.rotate(rotation);
        }

        FigureMover.getInstance().setFigure(null);
        splineOwner.notifyObservers();
    }

    public static void save(OutputStream stream, Map<Spline, SplineFigure3D> figures) {
        PrintStream printer = new PrintStream(stream);

        printer.print(splineOwner.getLengthCount());
        printer.print(" ");
        printer.print(splineOwner.getRotateCount());
        printer.print(" ");
        printer.print(splineOwner.getLengthK());
        printer.print(" ");
        printer.print(splineOwner.getLengthFrom());
        printer.print(" ");
        printer.print(splineOwner.getLengthTo());
        printer.print(" ");
        printer.print(splineOwner.getRotateFrom());
        printer.print(" ");
        printer.println(splineOwner.getRotateTo());

        printer.print(camera.getFrontZ());
        printer.print(" ");
        printer.print(camera.getBackZ());
        printer.print(" ");
        printer.print(camera.getWidth());
        printer.print(" ");
        printer.println(camera.getHeight());

        double[][] cameraRotation = camera.getRotation().getValues();
        printer.print(cameraRotation[0][0]);
        printer.print(" ");
        printer.print(cameraRotation[0][1]);
        printer.print(" ");
        printer.println(cameraRotation[0][2]);
        printer.print(cameraRotation[1][0]);
        printer.print(" ");
        printer.print(cameraRotation[1][1]);
        printer.print(" ");
        printer.println(cameraRotation[1][2]);
        printer.print(cameraRotation[2][0]);
        printer.print(" ");
        printer.print(cameraRotation[2][1]);
        printer.print(" ");
        printer.println(cameraRotation[2][2]);

        printer.print(camera.getColor().getRed());
        printer.print(" ");
        printer.print(camera.getColor().getGreen());
        printer.print(" ");
        printer.println(camera.getColor().getBlue());

        int K = figures.size();
        printer.println(K);

        for (Map.Entry<Spline, SplineFigure3D> entry : figures.entrySet()) {
            Spline spline = entry.getKey();
            Figure3D figure = entry.getValue();

            printer.print(spline.getColor().getRed());
            printer.print(" ");
            printer.print(spline.getColor().getGreen());
            printer.print(" ");
            printer.println(spline.getColor().getBlue());

            printer.print(figure.getCenter().getX());
            printer.print(" ");
            printer.print(figure.getCenter().getY());
            printer.print(" ");
            printer.println(figure.getCenter().getZ());

            double[][] rotation = figure.getRotation().getValues();
            printer.print(rotation[0][0]);
            printer.print(" ");
            printer.print(rotation[0][1]);
            printer.print(" ");
            printer.println(rotation[0][2]);
            printer.print(rotation[1][0]);
            printer.print(" ");
            printer.print(rotation[1][1]);
            printer.print(" ");
            printer.println(rotation[1][2]);
            printer.print(rotation[2][0]);
            printer.print(" ");
            printer.print(rotation[2][1]);
            printer.print(" ");
            printer.println(rotation[2][2]);

            int N = spline.getPoints().size();
            printer.println(N);

            for (Point2D point2D : spline.getPoints()) {
                printer.print(point2D.getX());
                printer.print(" ");
                printer.println(point2D.getY());
            }
        }
    }
}
