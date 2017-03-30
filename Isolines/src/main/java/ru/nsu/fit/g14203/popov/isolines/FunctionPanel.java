package ru.nsu.fit.g14203.popov.isolines;

import ru.nsu.fit.g14203.popov.util.SingleThreadPool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Reader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Supplier;
import java.util.stream.Stream;

class FunctionPanel extends JPanel {

    private final static int MAX_WIDTH = 1258;
    private final static int MAX_HEIGHT = 702;

    private Field2D function = new Field2D()/*.addCharge(1, 3, -1)
                                            .addCharge(5, 2, 0.7)
                                            .addCharge(1, 2, 1)*/
                                            .addCharge(0, 0, 4)
                                            /*.addCharge(-2, 1, -1.4)*/;

    private Legend legend = new Legend();
    private FunctionMap functionMap = new FunctionMap();

    private DoublePoint from = new DoublePoint();
    private DoublePoint to = new DoublePoint();

    private Color isolineColor;

    FunctionPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints(GridBagConstraints.RELATIVE, 0,
                1, 1,
                1, 1,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0);

        add(functionMap, constraints);

        constraints.weightx = 0.07;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(legend, constraints);

        SingleThreadPool threadPool = new SingleThreadPool();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (!legend.getFunctionLoaded().isTrue())
                    return;
                threadPool.execute(() -> functionMap.setFunction(from, to,
                                                                 function, legend, isolineColor));
            }
        });
    }

    void loadFunction(Reader reader) {
        Scanner scanner = new Scanner(reader);
        Supplier<String> nextLine = () -> {
            String result = "";
            while (result.isEmpty())
                result = scanner.nextLine().replaceAll("//.*", "");
            return result;
        };

        String gridXY = nextLine.get();

        int bordersCount = Integer.decode(nextLine.get());
        Color[] colors =  Stream.generate(() -> {
            int[] RGB = Arrays.stream(nextLine.get().split(" "))
                    .mapToInt(Integer::decode)
                    .toArray();
            return new Color(RGB[0], RGB[1], RGB[2]);
        }).limit(bordersCount + 1).toArray(Color[]::new);

        int[] RGB = Arrays.stream(nextLine.get().split(" "))
                .mapToInt(Integer::decode)
                .toArray();
        isolineColor = new Color(RGB[0], RGB[1], RGB[2]);

        double[] tmp = Arrays.stream(nextLine.get().split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
        from.setLocation(tmp[0], tmp[1]);

        tmp = Arrays.stream(nextLine.get().split(" "))
                .mapToDouble(Double::parseDouble)
                .toArray();
        to.setLocation(tmp[0], tmp[1]);

        double dx = (to.getX() - from.getX()) / (MAX_WIDTH - 1);
        double dy = (to.getY() - from.getY()) / (MAX_HEIGHT - 1);

        tmp = function.getMinMax(from, to, dx, dy);
        double min = tmp[0];
        double max = tmp[1];

        legend.setFunction(min, max, colors);
        functionMap.setFunction(from, to,
                                function, legend, isolineColor);
    }
}
