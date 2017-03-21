package ru.nsu.fit.g14203.popov.filter.filters.rendering;

import ru.nsu.fit.g14203.popov.filter.filters.Filter;
import ru.nsu.fit.g14203.popov.util.State;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

public class Rendering3DFilter implements Filter {

    private int sizeX = 350;
    private int sizeY = 350;
    private int sizeZ = 350;

    private State configLoaded = new State(false);
    private State absorptionEnable = new State(true);
    private State emissionEnable = new State(true);

    private Chart absorption = new Chart(1, absorptionEnable);
    private Chart[] emission = { new Chart(255, emissionEnable),        //  R
                                 new Chart(255, emissionEnable),        //  G
                                 new Chart(255, emissionEnable) };      //  B
    private Field field;

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public void setSizeZ(int sizeZ) {
        this.sizeZ = sizeZ;
    }

    public State getConfigLoaded() {
        return configLoaded;
    }

    public State getAbsorptionEnable() {
        return absorptionEnable;
    }

    public State getEmissionEnable() {
        return emissionEnable;
    }

    public void config(InputStream stream) {
        Chart __absorption = new Chart(1, absorptionEnable);
        Chart[] __emission = { new Chart(255, emissionEnable),        //  R
                               new Chart(255, emissionEnable),        //  G
                               new Chart(255, emissionEnable) };      //  B
        Field __field;

        Scanner scanner = new Scanner(stream);
        Supplier<String> nextLine = () -> {
            String line = "";
            while (line.isEmpty())
                line = scanner.nextLine().replaceAll("//.*", "");
            return line;
        };

//        ------   absorption   ------
        int N = Integer.decode(nextLine.get());
        for (int i = 0; i < N; i++) {
            double tmp[] = Arrays.stream(nextLine.get().split(" "))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            __absorption.addValue((int) tmp[0], tmp[1]);
        }

        __absorption.end();

//        ------   emission   ------
        N = Integer.decode(nextLine.get());
        for (int i = 0; i < N; i++) {
            int tmp[] = Arrays.stream(nextLine.get().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
            __emission[0].addValue(tmp[0], tmp[1]);
            __emission[1].addValue(tmp[0], tmp[2]);
            __emission[2].addValue(tmp[0], tmp[3]);
        }

        for (Chart chart : __emission)
            chart.end();

        __field = new Field(scanner);

        absorption = __absorption;
        emission = __emission;
        field = __field;

        configLoaded.setState(true);
    }

    public void clear() {
        configLoaded.setState(false);
        absorptionEnable.setState(true);
        emissionEnable.setState(true);
    }

    @Override
    public BufferedImage apply(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        result.setData(image.getData());

        field.computeMaxMin(sizeX, sizeY, sizeZ);

        CyclicBarrier barrier = new CyclicBarrier(2);
        new Thread(() -> compute(0, result, barrier)).start();
        compute(1, result, barrier);

        return result;
    }

    private void compute(int num, BufferedImage result, CyclicBarrier barrier) {
        int lowerBound = (int) (sizeX / 2.0 * num + 0.5);
        int upperBound = (int) (sizeX / 2.0 * (num + 1) + 0.5);

        double max = field.getMax();
        double min = field.getMin();
        double step = (max - min) / 100.0;

        double __sizeZ = 1.0 / sizeZ;

        for (int vX = lowerBound; vX < upperBound; vX++) {
            double __x = 1.0 / (sizeX * 2.0) * (2 * vX + 1);
            if (__x * 350 > result.getWidth())
                break;

            for (int vY = 0; vY < sizeY; vY++) {
                double __y = 1.0 / (sizeY * 2.0) * (2 * vY + 1);
                if (__y * 350 > result.getHeight())
                    break;

                double aValue = 1;
                double[] eValue = { 0, 0, 0 };    //  [R, G, B]

                for (int vZ = 0; vZ < sizeZ; vZ++) {
                    double __z = 1.0 / (sizeZ * 2.0) * (2 * vZ + 1);

                    double percent = (field.getValueAt(__x, __y, __z) - min) / step;

                    double aStep = Math.exp(-absorption.getValue(percent) * __sizeZ);
                    aValue *= aStep;
                    if (aValue < 0.5 / 0xFF)
                        break;

                    eValue[0] = eValue[0] * aStep + emission[0].getValue(percent) * __sizeZ;
                    eValue[1] = eValue[1] * aStep + emission[1].getValue(percent) * __sizeZ;
                    eValue[2] = eValue[2] * aStep + emission[2].getValue(percent) * __sizeZ;
                }

                if (aValue < 0.5 / 0xFF)
                    continue;
                applyPart(vX, vY, aValue, eValue, result);
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    private void applyPart(int vX, int vY, double aValue, double[] eValue, BufferedImage result) {
        int xLowerBound = (int) ((350.0 / sizeX) * vX + 0.5);   //  TODO: remove magic constant 350.0
        int xUpperBound = (int) ((350.0 / sizeX) * (vX + 1) + 0.5);

        int yLowerBound = (int) ((350.0 / sizeY) * vY + 0.5);
        int yUpperBound = (int) ((350.0 / sizeY) * (vY + 1) + 0.5);

        for (int x = xLowerBound; x < xUpperBound; x++) {
            if (x > result.getWidth())
                break;

            for (int y = yLowerBound; y < yUpperBound; y++) {
                if (y > result.getHeight())
                    break;

                int R = (result.getRGB(x, y) & 0xFF0000) / 0x010000;
                int G = (result.getRGB(x, y) & 0x00FF00) / 0x000100;
                int B = (result.getRGB(x, y) & 0x0000FF);

                R = (int) (R * aValue + eValue[0] + 0.5);
                R = (R < 0) ? 0
                            : (R > 0xFF) ? 0xFF
                                         : R;
                G = (int) (G * aValue + eValue[1] + 0.5);
                G = (G < 0) ? 0
                            : (G > 0xFF) ? 0xFF
                                         : G;
                B = (int) (B * aValue + eValue[2] + 0.5);
                B = (B < 0) ? 0
                            : (B > 0xFF) ? 0xFF
                                         : B;

                int RGB = R * 0x010000
                        + G * 0x000100
                        + B;
                result.setRGB(x, y, RGB);
            }
        }
    }
}
