package ru.nsu.fit.g14203.popov.filter.filters.rendering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Supplier;

class Field {

    private class Charge {
        double x;
        double y;
        double z;
        double q;

        Charge(double[] values) {
            this.x = values[0];
            this.y = values[1];
            this.z = values[2];
            this.q = values[3];
        }

        double influenceOn(double __x, double __y, double __z) {
            double r = Math.sqrt( (x - __x) * (x - __x) +
                                  (y - __y) * (y - __y) +
                                  (z - __z) * (z - __z) );
            r = (r < 0.1) ? 0.1 : r;
            return q / r;
        }
    }

    private ArrayList<Charge> charges = new ArrayList<>();

    private int sizeX;
    private int sizeY;
    private int sizeZ;

    private double max;
    private double min;

    Field(Scanner scanner) {
        Supplier<String> nextLine = () -> {
            String line = "";
            while (line.isEmpty())
                line = scanner.nextLine().replaceAll("//.*", "");
            return line;
        };

        int N = Integer.decode(nextLine.get());
        for (int i = 0; i < N; i++) {
            double[] tmp = Arrays.stream(nextLine.get().split(" "))
                    .mapToDouble(Double::parseDouble)
                    .toArray();
            charges.add(new Charge(tmp));
        }
    }

    void computeMaxMin(int sizeX, int sizeY, int sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;

        double[][] data = new double[2][2];     //  [num][max, min]

        CyclicBarrier barrier = new CyclicBarrier(2);
        new Thread(() -> computePartMinMax(0, data[0], barrier)).start();
        computePartMinMax(1, data[1], barrier);

        max = Math.max(data[0][0], data[1][0]);
        min = Math.min(data[0][1], data[1][1]);
    }

    private void computePartMinMax(int num, double[] data, CyclicBarrier barrier) {
        int lowerBound = (int) (sizeX / 2.0 * num + 0.5);
        int upperBound = (int) (sizeX / 2.0 * (num + 1) + 0.5);

        data[0] = Double.MIN_VALUE;     //  max
        data[1] = Double.MAX_VALUE;     //  min

        for (int vX = lowerBound; vX < upperBound; vX++) {
            for (int vY = 0; vY < sizeY; vY++) {
                for (int vZ = 0; vZ < sizeZ; vZ++) {
                    double __x = 1.0 / (sizeX * 2.0) * (2 * vX + 1);
                    double __y = 1.0 / (sizeY * 2.0) * (2 * vY + 1);
                    double __z = 1.0 / (sizeZ * 2.0) * (2 * vZ + 1);
                    double value = getValueAt(__x, __y, __z);

                    data[0] = (data[0] > value) ? data[0] : value;
                    data[1] = (data[1] < value) ? data[1] : value;
                }
            }
        }

        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    double getMax() {
        return max;
    }

    double getMin() {
        return min;
    }

    double getValueAt(double x, double y, double z) {
        return charges.stream()
                .mapToDouble(c -> c.influenceOn(x, y, z))
                .sum();
    }
}
