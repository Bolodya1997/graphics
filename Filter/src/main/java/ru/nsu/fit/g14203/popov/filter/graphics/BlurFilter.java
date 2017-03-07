package ru.nsu.fit.g14203.popov.filter.graphics;

public class BlurFilter extends MatrixFilter {

    @Override
    int[][] getK() {
        return new int[][]{ { 0, 1, 0 },
                            { 1, 2, 1 },
                            { 0, 1, 0 } };
    }

    @Override
    int getW() {
        return 6;
    }
}
