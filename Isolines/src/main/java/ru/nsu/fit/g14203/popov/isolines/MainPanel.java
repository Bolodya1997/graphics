package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.State;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Reader;
import java.util.Arrays;
import java.util.Observer;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

class MainPanel extends JPanel {

    private final static int MAX_WIDTH = 1258;
    private final static int MAX_HEIGHT = 702;

    private final static Point2D.Double DEFAULT_FROM = new Point2D.Double(0, 0);
    private final static Point2D.Double DEFAULT_TO = new Point2D.Double(5, 5);

    private State isolinesShown = new State(true);
    private State gridShown = new State(true);
    private State pointsShown = new State(false);
    private State interpolationOn = new State(false);

    private Function2D function = new Function2D() {
        @Override
        double getValue(double x, double y) {
            double X = x * 0.5 + y * 0.5;
            double Y = -x * 0.5 + y * 0.5;

            return X * X * Math.cos(X) - Y * Y * Math.sin(Y);
        }
    };

    private Legend legend = new Legend(interpolationOn);
    private FunctionMap functionMap;

    private Point2D.Double from;
    private Point2D.Double to;

    private int gridWidth;
    private int gridHeight;
    private Color[] colors;
    private Color isolineColor;

    MainPanel(Consumer<String> showInStatusBar, Runnable clearStatusBar) {
        functionMap = new FunctionMap(isolinesShown,
                                      gridShown, pointsShown,
                                      showInStatusBar, clearStatusBar);

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0,
                1, 1,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(15, 15, 15, 15), 0, 0);

        add(functionMap, constraints);

        constraints.weightx = 0.07;
        constraints.insets = new Insets(15, 0, 15, 15);
        add(legend, constraints);

        Observer functionMapRepaintObserver = (o, arg) -> SwingUtilities.invokeLater(functionMap::repaint);
        gridShown.addObserver(functionMapRepaintObserver);
        pointsShown.addObserver(functionMapRepaintObserver);

        Observer functionMapRecountObserver = (o, arg) -> {
            new Thread(functionMap::recount).start();
        };
        isolinesShown.addObserver(functionMapRecountObserver);
        interpolationOn.addObserver(functionMapRecountObserver);
    }

    void loadFunction(Reader reader) {
        Scanner scanner = new Scanner(reader);
        Supplier<String> nextLine = () -> {
            String result = "";
            while (result.isEmpty())
                result = scanner.nextLine().replaceAll("//.*", "");
            return result;
        };

        String[] gridWH = nextLine.get().split(" ");
        gridWidth = Integer.decode(gridWH[0]);
        gridHeight = Integer.decode(gridWH[1]);

        int bordersCount = Integer.decode(nextLine.get());
        colors =  Stream.generate(() -> {
            int[] RGB = Arrays.stream(nextLine.get().split(" "))
                    .mapToInt(Integer::decode)
                    .toArray();
            return new Color(RGB[0], RGB[1], RGB[2]);
        }).limit(bordersCount + 1).toArray(Color[]::new);

        int[] RGB = Arrays.stream(nextLine.get().split(" "))
                .mapToInt(Integer::decode)
                .toArray();
        isolineColor = new Color(RGB[0], RGB[1], RGB[2]);

        from = DEFAULT_FROM;
        to = DEFAULT_TO;
        setArea();
    }

    void setArea() {
        double dx = (to.getX() - from.getX()) / (MAX_WIDTH - 1);
        double dy = (to.getY() - from.getY()) / (MAX_HEIGHT - 1);

        double[] tmp = function.getMinMax(from, to, dx, dy);
        double min = tmp[0];
        double max = tmp[1];

//        ------   set new function   ------
        legend.setFunction(min, max, colors);
        functionMap.setFunction(from, to,
                                function, legend,
                                gridWidth, gridHeight,
                                isolineColor);
    }

//    ------   getters   ------

    State getFunctionLoaded() {
        return legend.getFunctionLoaded();
    }

    State getIsolinesShown() {
        return isolinesShown;
    }

    State getGridShown() {
        return gridShown;
    }

    State getPointsShown() {
        return pointsShown;
    }

    State getInterpolationOn() {
        return interpolationOn;
    }

    void clearIsoline() {
        functionMap.clearIsoline();
    }

    Point2D.Double getFrom() {
        return from;
    }

    Point2D.Double getTo() {
        return to;
    }
}
