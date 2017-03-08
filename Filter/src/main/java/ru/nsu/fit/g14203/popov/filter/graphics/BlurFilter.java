package ru.nsu.fit.g14203.popov.filter.graphics;

public class BlurFilter extends MatrixFilter {

    @Override
    int[][] getMatrix() {
        return new int[][]{ { 0, 1, 0 },
                            { 1, 2, 1 },
                            { 0, 1, 0 } };
    }

    @Override
    int getDivider() {
        return 6;
    }

    @Override
    int getOffset() {
        return 0;
    }
}
